package com.flipped.seckill.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/9
 * Time: 21:03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private TUser user;
    private Long goodId;
}
