package com.flipped.seckill.vo;


import com.flipped.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

/**
 * Description: 接收传递过来的参数
 * User: zh
 * Date: 2021/7/4
 * Time: 13:12
 */

@Data
public class LoginVo {
    @NotNull
    @IsMobile(required = true)
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
