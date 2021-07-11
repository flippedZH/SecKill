package com.flipped.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 * @Classname 公共返回对象枚举
 * @Description TODO
 * @Date 2021/7/4 11:45
 * @Created by zh
 */

@AllArgsConstructor
@Getter
@ToString
public enum  RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    //登陆模块
    LOGIN_ERROR(500100,"密码或用户名错误"),
    MOBILE_ERROR(500200,"号码格式不对"),
    BIND_ERROR(500300,"参数校验异常"),

    //秒杀模块
    EMPTY_STOCK(500400,"库存不足"),
    REPEATE_ERROR(500500,"一人一件"),
    REQURST_ILLEGAL(500900,"请求非法"),
    ERROR_CAPTCHA(501000,"验证码错误"),
    SESSION_ERROR(1111111,"用户未登录"),
    ACCESS_LIMIT_REACHED(501100,"访问过于频繁，稍后再试"),


    //密码更新
    MOBILE_NOT_EXIST(500600,"手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500700,"密码更新失败"),

    //订单信息
    ORDER_NOT_EXIST(500800,"订单信息不存在"),

    ;

    private final Integer code;
    private final String message;



//    RespBeanEnum(Integer code, String message) {
//        this.code = code;
//        this.message = message;
//    }
//
//    public Integer getCode() {
//        return code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
}
