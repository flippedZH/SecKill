package com.flipped.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flipped.seckill.pojo.TOrder;
import com.flipped.seckill.pojo.TUser;
import com.flipped.seckill.vo.GoodsVo;
import com.flipped.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author flipped
 * @since 2021-07-06
 */
public interface ITOrderService extends IService<TOrder> {

    TOrder seckill(TUser user, GoodsVo goodsVo);

    OrderDetailVo getOrdeDetail(Long orderId);

    String createPath(TUser user, Long goodsId);

    boolean checkPath(TUser user, Long goodsId, String path);

    boolean checkCaptcha(TUser user, Long goodsId, String captcha);
}
