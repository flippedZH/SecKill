package com.flipped.seckill.vo;


import com.flipped.seckill.pojo.TOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/8
 * Time: 11:07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {

    private TOrder order;
    private GoodsVo goodsVo;

}
