package com.flipped.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.flipped.seckill.pojo.TGoods;
import com.flipped.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author flipped
 * @since 2021-07-06
 */
public interface ITGoodsService extends IService<TGoods> {

    Object getGoodsVo();

    GoodsVo getGoodsById(Long goodsId);
}
