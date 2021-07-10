package com.flipped.seckill.vo;


import com.flipped.seckill.utils.ValidatorUtils;
import com.flipped.seckill.validator.IsMobile;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Description: no desc
 * User: zh
 * Date: 2021/7/4
 * Time: 20:57
 */

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean require;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        //接收是否为必填的 参数
        require=constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //如果是必填的
        if(require){
            return ValidatorUtils.isMobile(value);//带判空
            //如果不是必填的
        }else {
            //允许为空
            if(StringUtils.isEmpty(value)){
                return true;
                //不为空 那么一定要是有效的
            }else {
                return ValidatorUtils.isMobile(value);
            }
        }
    }
}
