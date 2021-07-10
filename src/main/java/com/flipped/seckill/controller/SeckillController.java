package com.flipped.seckill.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flipped.seckill.pojo.SeckillMessage;
import com.flipped.seckill.pojo.TOrder;
import com.flipped.seckill.pojo.TSeckillOrder;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.rabbitmq.MQSender;
import com.flipped.seckill.service.impL.TGoodsServiceImpl;
import com.flipped.seckill.service.impL.TOrderServiceImpl;
import com.flipped.seckill.service.impL.TSeckillOrderServiceImpl;
import com.flipped.seckill.vo.GoodsVo;
import com.flipped.seckill.vo.RespBean;
import com.flipped.seckill.vo.RespBeanEnum;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/6
 * Time: 17:15
 */

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private TSeckillOrderServiceImpl seckillOrderService;

    @Autowired
    private TGoodsServiceImpl goodsService;

    @Autowired
    private TOrderServiceImpl orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqsender;

    private Map<Long,Boolean> EmptyStockMap=new HashMap<>();

    //系统初始化 时候 将商品库存数量加载到Redis
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo>  list=goodsService.getGoodsVo();
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //遍历元素 并用元素实现相应的功能
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(),false);
        });
    }


    //goodsId 自动映射
    @RequestMapping("/doSeckill0")
    public String doSeckill0(Model model, TUser user, Long goodsId){
        System.out.println("我拿到了user:"+user);
        if(null==user){
            return "login";
        }
        model.addAttribute("user",user);
        GoodsVo goodsVo=goodsService.getGoodsById(goodsId);

        //判断库存
        if(goodsVo.getStockCount()<1){
            //给前端反馈信息
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            model.addAttribute("errmsg", RespBean.error(RespBeanEnum.EMPTY_STOCK).getMessage());
            return "seckillFail";
        }

        //判断是否为重复抢购--根据秒杀订单中的商品id与用户id来判断
        //从订单池中查询一条记录 不为空就 不允许秒杀（线程安全？）-----异常（如果用户很久以前已经秒杀过一件 这里会查询出多条 抛出异常）
        TSeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<TSeckillOrder>().
                eq("user_id", user.getId()).eq("goods_id", goodsId));
        if(seckillOrder!=null){
            model.addAttribute("errmsg",RespBean.error(RespBeanEnum.REPEATE_ERROR).getMessage());
            return "seckillFail";
        }

        //商品的订单信息 （完整信息）
        //秒杀的订单信息
        TOrder order=orderService.seckill(user,goodsVo);
        System.out.println("order:"+order);
        model.addAttribute("order",order);
        model.addAttribute("goods",goodsVo);
        return "orderDetail";
    }


    //页面静态化
    @RequestMapping(value = "/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill( TUser user, Long goodsId){
        System.out.println("我拿到了user:"+user);
        if(null==user){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        //在redis中获取订单数据
        TSeckillOrder seckillOrder= (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);

        //判断是否重复订单
        if(seckillOrder!=null){
            //确实是重复抢购
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        //通过内存标记，减少Redis的访问
        if(EmptyStockMap.get(goodsId)){
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        ValueOperations valueOperations =redisTemplate.opsForValue();
        //原子性 每调用一次减一
        Long  stock =valueOperations.decrement("seckillGoods:"+goodsId);

        if(stock<0){
            EmptyStockMap.put(goodsId,true);
            //库存置0
            valueOperations.increment("seckillGoods:"+goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //存入数据库之前 封装消息对象(包括user 和 订单信息)

        SeckillMessage secKillMessage =new SeckillMessage(user,goodsId);
        String s = JSONObject.toJSONString(secKillMessage);
        //消息入队列
        mqsender.sendSeckillMsg(s);
        //给前端信号 前端显示正在排队中

        return RespBean.success(0);
    }


    //orderid ：成功  -1：秒杀失败  0：排队中
    //获取秒杀结果
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(TUser user,Long goodsId){
        //必须是登陆态
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        //查询有没有生成秒杀订单
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }
}
