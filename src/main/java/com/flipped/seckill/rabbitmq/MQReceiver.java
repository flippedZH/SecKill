package com.flipped.seckill.rabbitmq;



import com.alibaba.fastjson.JSONObject;
import com.flipped.seckill.pojo.SeckillMessage;
import com.flipped.seckill.pojo.TOrder;
import com.flipped.seckill.pojo.TSeckillOrder;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.impL.TGoodsServiceImpl;
import com.flipped.seckill.service.impL.TOrderServiceImpl;
import com.flipped.seckill.vo.GoodsVo;
import com.flipped.seckill.vo.RespBean;
import com.flipped.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Description: 消息消费者
 * User: zh
 * Date: 2021/7/9
 * Time: 11:02
 */

@Service
@Slf4j
public class MQReceiver {

//    @RabbitListener(queues = "queue")
//    public void receive(Object msg){
//        log.info("接收的消息："+msg);
//    }
//
//    //fanout模式
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg){
//        log.info("queue01接收的消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg){
//        log.info("queue02接收的消息："+msg);
//    }
//
//    //direct模式
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg){
//        log.info("queue01接收的消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg){
//        log.info("queue02接收的消息："+msg);
//    }
//
//    //topic模式
//    @RabbitListener(queues = "queue_top01")
//    public void receive05(Object msg){
//        log.info("queue01接收的消息："+msg);
//    }
//
//    @RabbitListener(queues = "queue_top02")
//    public void receive06(Object msg){
//        log.info("queue02接收消息："+msg);
//    }

    @Autowired
    private TGoodsServiceImpl goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TOrderServiceImpl orderService;

    @RabbitListener(queues = "seckillQueue")
    public void receive(String  msg){
        log.info("用户接收消息："+msg);
        SeckillMessage seckillMessage =JSONObject.parseObject(msg,SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodId();
        TUser user = seckillMessage.getUser();
        //数据库操作
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);

        if(goodsVo.getStockCount()<1){
            return;
        }
        //在redis中比对订单数据  ??????
        TSeckillOrder seckillOrder= (TSeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        //判断是否重复订单
        if(seckillOrder!=null){
            //确实是重复抢购
            return ;
        }

        //下单操作
        TOrder order=orderService.seckill(user,goodsVo);
    }
}


