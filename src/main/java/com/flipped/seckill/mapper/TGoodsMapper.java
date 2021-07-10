package com.flipped.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.flipped.seckill.pojo.TGoods;
import com.flipped.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 * @author flipped
 * @since 2021-07-06
 */
public interface TGoodsMapper extends BaseMapper<TGoods> {

    List<GoodsVo> selectGoodsList();

    GoodsVo selectGoodsById(Long goodsId);
}
