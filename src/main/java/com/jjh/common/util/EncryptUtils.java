package com.jjh.common.util;

import org.apache.shiro.crypto.hash.Sha256Hash;

/**
 * 加密工具类
 *
 * @author jjh
 * @date 2019/11/27
 **/
public class EncryptUtils {

    /**
     * 用户密码加密
     * @param username  用户名
     * @param password  密码
     * @param salt  密盐
     * @return
     */
    public static String encryptPassword(String username, String password, String salt) {
        return new Sha256Hash(password, username + salt).toString();
    }


}
