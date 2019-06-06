package com.framework.config;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /*解决axios等的预检请求 https://www.jianshu.com/p/5c637bfcc674*/
        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            // 此处配置的是允许任意域名跨域请求，可根据需求指定
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "86400");
            response.setHeader("Access-Control-Allow-Headers", "*");
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
        try {
            if (isProtectedUrl(request)) {
                if (request.getAttribute("user") == null) {
                    throw new IllegalStateException("user is null. ");
                }
            }
        }
        catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
            return;
        }

        filterChain.doFilter(request,response);
    }

    /**
     * 拦截/api开头的接口进行token校验
     */
    private boolean isProtectedUrl(HttpServletRequest request){
        return  pathMatcher.match("/api/**",request.getServletPath());
    }

}
