package com.flipped.seckill.exception;


import com.flipped.seckill.vo.RespBean;
import com.flipped.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Description: 全局异常处理类
 * User: zh
 * Date: 2021/7/4
 * Time: 21:36
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e){
        if(e instanceof  GlobalException){
            GlobalException ex=(GlobalException) e;
            return  RespBean.error(ex.getRespBeanEnum());
        }else if(e instanceof BindException){
            BindException bindException=(BindException)e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常"+bindException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        return RespBean.error(RespBeanEnum.ERROR);
    }
}
