package com.jjh.framework.config;

import com.jjh.common.util.request.BodyReaderHttpServletRequestWrapper;
import com.jjh.framework.context.TenantContextHolder;
import com.jjh.framework.jwt.JWTConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 跨域请求拦截器（解决跨域问题）
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*解决axios等的预检请求 https://www.jianshu.com/p/5c637bfcc674 */
        // 此处配置的是允许任意域名跨域请求，可根据需求指定
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "86400");
        response.setHeader("Access-Control-Allow-Headers", "*");
        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }

        //JWT验证
        /*try {
            if (isProtectedUrl(request)) {
                request = JwtUtils.validateTokenAndAddUserToHeader(request);
            }
        }
        catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            return;
        }*/

        //session验证
//        try {
//            if (isProtectedUrl(request)) {
//                if (request.getAttribute("user") == null) {
//                    throw new IllegalStateException("user is null. ");
//                }
//            }
//        }
//        catch (Exception e){
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
//            return;
//        }

        // 租户处理
        String tenantCode = request.getHeader(JWTConstants.X_TENANT_AUTH);
        if (tenantCode != null) {
            TenantContextHolder.setTenant(tenantCode);
        }

        BodyReaderHttpServletRequestWrapper requestWrapper = null;
        if(request instanceof HttpServletRequest) {
            //request解决getInputStream读取一次问题（处理请求参数打印）
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }
        //获取请求中的流，将取出来的字符串，再次转换成流，然后把它放入到新request对象中。
        // 在chain.doFiler方法中传递新的request对象
        if(requestWrapper == null) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(requestWrapper, response);
        }
    }

    /**
     * 拦截/api开头的接口进行token校验
     */
    private boolean isProtectedUrl(HttpServletRequest request){
        return  pathMatcher.match("/api/**",request.getServletPath());
    }

}
