package com.flipped.seckill.utils;


import java.util.UUID;

/**
 * Description: UUID工具类
 * User: zh
 * Date: 2021/7/5
 * Time: 10:19
 */


public class UUIDUtil {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
