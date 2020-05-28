package com.jjh.framework.context;

import cn.hutool.core.util.StrUtil;

/**
 * 租户上下文（实现动态数据源设置的关键）
 * 当用户请求系统资源时，我们将用户提供的租户信息（tenantId）存放在ThreadLoacal中，
 * 紧接着获取TheadLocal中的租户信息，并根据此信息查询单独的租户库，获取当前租户的数据配置信息，
 * http://blog.didispace.com/saas-design-by-spring-boot-application/
 *
 * @author jjh
 * @date 2020/2/25
 **/
public class TenantContextHolder {

    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    /**
     * 设置租户信息到上下文中
     * @param tenant 租户信息
     */
    public static void setTenant(String tenant) {
        CONTEXT.set(tenant);
    }

    /**
     * 获取租户信息
     * @return
     */
    public static String getTenant() {
        return CONTEXT.get();
    }

    /**
     * 清空租户信息
     */
    public static void clear() {
        CONTEXT.remove();
    }

    /**
     * 获取租户缓存前缀
     * @return
     */
    public static String getTenantRedisKey() {
        if (StrUtil.isBlank(CONTEXT.get())) {
            return "";
        }
        return CONTEXT.get() + ":";
    }
}
