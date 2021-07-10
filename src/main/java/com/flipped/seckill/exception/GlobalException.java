package com.flipped.seckill.exception;


import com.flipped.seckill.vo.RespBeanEnum;
import lombok.Data;

/**
 * Description: 全局异常
 * User: zh
 * Date: 2021/7/4
 * Time: 21:36
 */

@Data
public class GlobalException  extends  RuntimeException{
    private RespBeanEnum respBeanEnum;
    public GlobalException(RespBeanEnum respBeanEnum) {
        this.respBeanEnum = respBeanEnum;
    }
}
