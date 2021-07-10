package com.flipped.seckill.vo;


import com.flipped.seckill.pojo.TUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/7
 * Time: 21:10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailVo {

    private TUser user;
    private GoodsVo goodsVo;
    private int secKillStatus;
    private  int remainSecond;
}
