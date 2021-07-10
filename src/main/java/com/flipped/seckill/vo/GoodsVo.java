package com.flipped.seckill.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.flipped.seckill.pojo.TGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 商品返回对象（商品详情+秒杀信息）
 * User: zh
 * Date: 2021/7/6
 * Time: 10:36
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo  extends TGoods {

//    private Long id;
//    private String goodsName;
//    private String goodsTitle;
//    private String goodsImg;
//    private String goodsDetail;
//    private BigDecimal goodsPrice;
//    private Integer goodsStock;

    private BigDecimal seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;

}
