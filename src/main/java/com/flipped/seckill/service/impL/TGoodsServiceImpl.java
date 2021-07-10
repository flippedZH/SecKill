package com.flipped.seckill.service.impL;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flipped.seckill.mapper.TGoodsMapper;
import com.flipped.seckill.pojo.TGoods;
import com.flipped.seckill.service.ITGoodsService;
import com.flipped.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author flipped
 * @since 2021-07-06
 */
@Service
public class TGoodsServiceImpl extends ServiceImpl<TGoodsMapper, TGoods> implements ITGoodsService {

    @Autowired
    private TGoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> getGoodsVo() {
        return  goodsMapper.selectGoodsList();
    }

    @Override
    public GoodsVo getGoodsById(Long goodsId) {
       return goodsMapper.selectGoodsById(goodsId);
    }
}
