package com.framework.mybatis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * mybatis基于Interceptor开发审计的自动更新（创建用户，创建日期，更新用户，更新日期）
 * https://my.oschina.net/stategrace/blog/347272
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { FIELD, METHOD, ANNOTATION_TYPE })
public @interface UpdateBy {

}
