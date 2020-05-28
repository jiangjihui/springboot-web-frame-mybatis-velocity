package com.jjh.common.util;

/**
 * ID生成器
 *
 * @author jjh
 * @date 2019/9/20
 */
public class IdGenerateHelper {

    /**
     * 获取ID
     * @return
     */
    public static String nextId() {
//        return UUID.randomUUID().toString();
        return SnowFlake.nextId();
    }
}
