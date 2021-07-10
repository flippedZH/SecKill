package com.flipped.seckill.utils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: 手机号码校验（11位）
 * User: zh
 * Date: 2021/7/4
 * Time: 17:05
 */

public class ValidatorUtils {

    private static String mobileRex = "^(1)\\d{10}$";
    private static final Pattern mobilePattern = Pattern.compile(mobileRex);

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher matcher = mobilePattern.matcher(src);
        return matcher.matches();

    }
}
