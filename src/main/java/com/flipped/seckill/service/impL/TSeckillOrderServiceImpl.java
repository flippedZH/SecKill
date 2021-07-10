package com.flipped.seckill.service.impL;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.seckill.mapper.TSeckillOrderMapper;
import com.flipped.seckill.pojo.TSeckillOrder;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.service.ITSeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author flipped
 * @since 2021-07-06
 */

//orderid ：成功  -1：秒杀失败  0：排队中
@Service
public class TSeckillOrderServiceImpl extends ServiceImpl<TSeckillOrderMapper, TSeckillOrder> implements ITSeckillOrderService {


    @Autowired
    private TSeckillOrderServiceImpl seckillOrderService;

    @Autowired TSeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    //获取秒杀结果
    @Override
    public Long getResult(TUser user, Long goodsId) {
        //就是从数据库中查询满足给定两个字段内容的 目标（通过eq动态扩展）
        TSeckillOrder seckillOrder = seckillOrderMapper.
                selectOne(new QueryWrapper<TSeckillOrder>().
                        eq("user_id", user.getId()).eq("goods_id", goodsId));

        //判断逻辑：
        //直接查到了订单---肯定是秒杀成功了的
        if(null!=seckillOrder){
            return seckillOrder.getOrderId();
        //可能rabbitmq中的消息还没完全出队,但后面的消息都无法下单！
        }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
            return -1L;
        }else{
            //不要急 看看你前面的同志出队后还有没有库存 0.0
            return 0L;
        }
    }
}
