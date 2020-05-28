package com.jjh.common.key;

/**
 * 系统相关redisKey
 *
 * @author jjh
 * @date 2019/11/29
 **/
public class CacheConstants {

    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String SYS_USER_TOKEN  = "sys:user:token";

    /** 用户名缓存用户信息 */
    public static final String SYS_USER_NAME  = "sys:user:name:";

    /** 用户ID缓存用户信息 */
    public static final String SYS_USER_ID  = "sys:user:id:";

    /** 系统基础字典信息缓存 */
    /* 地区*/
    public static final String SYS_BASE_DICT_PROVINCIAL_CODE  = "sys:base:dict:provincial:code:";
    /* 地区列表*/
    public static final String SYS_BASE_DICT_PROVINCIAL_CODESTR  = "sys:base:dict:provincial:codestr:";
    /* 字典数据*/
    public static final String SYS_BASE_DICT_TYPE_DATA_LIST  = "sys:base:dict:type:data:list:";


    /** 业务信息缓存 */

}
