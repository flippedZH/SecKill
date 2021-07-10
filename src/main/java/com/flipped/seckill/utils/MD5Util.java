package com.flipped.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    //静态方法加密
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //盐
    private static final String salt = "1a2b3c4d";

    //前端加密
    //混淆salt  12 + password + c3
    public static String inputPwd2FrontPwd(String inputPwd) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(salt.charAt(0));
        sb.append(salt.charAt(2));
        sb.append(inputPwd);
        sb.append(salt.charAt(5));
        sb.append(salt.charAt(3));
        return md5(sb.toString());
    }

    /**
     * 前端加密后的密码到数据库的密码
     *
     * @param frontPwd
     * @param salt     存储在数据库中
     * @return
     */
    //后端加密
    public static String frontPwdToDBPwd(String frontPwd, String salt) {
        StringBuilder sb = new StringBuilder();
        sb.append(salt.charAt(1));
        sb.append(salt.charAt(3));
        sb.append(frontPwd);
        sb.append(salt.charAt(4));
        sb.append(salt.charAt(7));
        return md5(sb.toString());
    }

    //get最终加密结果 --用于存入数据库
    public static String inputPwd2DBPwd(String inputPwd, String salt) {
        String frontPwd = inputPwd2FrontPwd(inputPwd);
        String dbPwd = frontPwdToDBPwd(frontPwd, salt);
        return dbPwd;
    }

    //测试
    public static void main(String[] args) {
        String frontPwd = inputPwd2FrontPwd("123456");
        String dbPwd = frontPwdToDBPwd(frontPwd, "1a2b3c4d");
        System.out.println(frontPwd);
        System.out.println(dbPwd);
        System.out.println(inputPwd2DBPwd("123456", "1a2b3c4d"));
    }
}
