package com.flipped.seckill.service.impL;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.seckill.exception.GlobalException;
import com.flipped.seckill.mapper.TGoodsMapper;
import com.flipped.seckill.mapper.TOrderMapper;
import com.flipped.seckill.pojo.TOrder;
import com.flipped.seckill.pojo.TSeckillGoods;
import com.flipped.seckill.pojo.TSeckillOrder;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.ITOrderService;
import com.flipped.seckill.vo.GoodsVo;
import com.flipped.seckill.vo.OrderDetailVo;
import com.flipped.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  秒杀服务实现类
 * </p>
 *
 * @author flipped
 * @since 2021-07-06
 */

@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements ITOrderService {

    @Autowired
    private  TSeckillGoodsServiceImpl seckillGoodsService;

    @Autowired
    private  TOrderMapper orderMapper;

    @Autowired
    private TGoodsMapper goodsMapper;

    @Autowired
    private  TSeckillOrderServiceImpl seckillOrderService;


    @Autowired
    private TGoodsServiceImpl goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    //秒杀服务实现方法
    @Override
    public TOrder seckill(TUser user, GoodsVo goodsVo) {

        //秒杀商品表减库存 (先查询完 后减库)
        TSeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<TSeckillGoods>().eq("goods_id", goodsVo.getId()));

        //扣库存
        //直接在数据库进行操作 （sql语句）
        boolean updateRes = seckillGoodsService.update(new UpdateWrapper<TSeckillGoods>()
                .setSql("stock_count=" + "stock_count-1")  //执行sql语句
                //执行条件
                .eq("goods_id", goodsVo.getId())    //等于
                .gt("stock_count", 0));         //大于0

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断库存
        if(seckillGoods.getStockCount()<1){
            //判断是否还有库存
            //没有就把isStockEmpty设置为0
                valueOperations.set("isStockEmpty:"+goodsVo.getId(),"0");
        }

//        //扣除失败返回null,扣除成功就生成订单
//        if(!updateRes){
//            return null;
//        }

        //生成订单
        TOrder order =new TOrder();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        //长整型0
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        //????
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        TSeckillOrder seckillOrder=new TSeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrderService.save(seckillOrder);
        //秒杀订单存入redis
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goodsVo.getId(),seckillOrder);
        return order;
    }


    @Override
    public OrderDetailVo getOrdeDetail(Long orderId) {

        if(orderId==null){
            throw  new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }

        //查询order
        TOrder order = orderMapper.selectById(orderId);//默认方法 不用自己写~~~~~~

        //查询goodVo
        GoodsVo goodsVo=goodsService.getGoodsById(order.getGoodsId());

        OrderDetailVo detailVo=new OrderDetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setOrder(order);

        //拼接并返回
        return detailVo;
    }
}
