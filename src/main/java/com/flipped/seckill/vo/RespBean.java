package com.flipped.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Classname 公共返回对象:专门用返回错误与成功message
 * @Description TODO
 * @Date 2021/7/4 11:45
 * @Created by zh
 */

@ToString
@Data
@NoArgsConstructor
public class RespBean {
    private  long code;
    private String message;
    private Object obj;

    //成功返回结果  成功只有一种成功
    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
    }

    public static RespBean success(Object obj){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),obj);
    }

    //失败返回结果 失败却有多种失败 所以将枚举类传进去 然后选择里面的不同的错误类型
    public static RespBean error(RespBeanEnum respBeanEnum){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
    }

    public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
        return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
    }


    public RespBean(long code, String message, Object obj) {
        this.code = code;
        this.message = message;
        this.obj = obj;
    }
}
