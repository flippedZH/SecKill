package com.flipped.seckill.config;

import com.flipped.seckill.pojo.TUser;

/**
 * 存储用户信息到线程中
 */
public class UserContext {
    private static ThreadLocal<TUser> userHolder = new ThreadLocal<TUser>();

    public static void setUser(TUser user) {
        userHolder.set(user);
    }

    public static TUser getUser() {
        return userHolder.get();
    }
}
