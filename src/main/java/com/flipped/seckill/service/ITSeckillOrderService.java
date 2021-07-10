package com.flipped.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flipped.seckill.pojo.TSeckillOrder;
import com.flipped.seckill.pojo.TUser;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author flipped
 * @since 2021-07-06
 */
public interface ITSeckillOrderService extends IService<TSeckillOrder> {

    Long  getResult(TUser user, Long goodsId);

}
