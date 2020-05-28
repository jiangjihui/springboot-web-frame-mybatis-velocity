package com.jjh.common.util.request;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 *
 * @author jjh
 * @date 2020/4/26
 **/
public class RequestUtils {

    /**
     * 获取当前上下文中的请求
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }

    /**
     * 获取当前上下文中请求的IP地址
     * @return  IP地址
     */
    public static String currentRequestIP() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getRemoteAddr();
        }
        return null;
    }

    /**
     * 获取当前上下文中的请求地址
     * @return  请求地址
     */
    public static String currentRequestUrl() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getRequestURI();
        }
        return null;
    }

    /**
     * 获取当前上下文中的请求地址
     * @return  请求地址
     */
    public static String currentRequestMethod() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getMethod();
        }
        return null;
    }
}
