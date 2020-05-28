package com.jjh.framework.jpa;

/**
 * 复杂查询（字段）后缀
 *
 * @author jjh
 * @date 2019/12/12
 **/
public class SpecificSuffix {

    /** 是否为空*/
    public static final String IS_NULL = "_WithIsNull";

    /** 在范围内*/
    public static final String IN = "_WithIn";

    /** 大于等于*/
    public static final String GREAT_EQUAL = "_WithGreatEqual";

    /** 小于等于*/
    public static final String LESS_EQUAL = "_WithLessEqual";

    /** 模糊查询（默认前缀匹配）*/
    public static final String LIKE = "_WithLike";

    /** 模糊查询（全模糊匹配）*/
    public static final String LIKE_ALL = "_WithLikeAll";

}
