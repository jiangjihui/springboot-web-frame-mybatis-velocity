package com.jjh.framework.jwt;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.jjh.common.web.form.SimpleResponseForm;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  JWT过滤器
 * 因为 JWT 的整合，我们需要自定义自己的过滤器 JWTFilter，JWTFilter 继承了 BasicHttpAuthenticationFilter，并部分原方法进行了重写
 * https://www.jianshu.com/p/3c51832f1051
 *
 * @author jjh
 * @date 2019/11/25
 **/
public class JwtFilter extends BasicHttpAuthenticationFilter {



    /**
     * 执行登录认证
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        executeLogin(request, response);
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(JWTConstants.X_ACCESS_TOKEN);
        if (StrUtil.isBlank(token)) {
            throw new AuthenticationException("请求异常，缺少token");
        }
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request,response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 处理用户token验证失败异常
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try {
            return super.preHandle(request, response);
        } catch (AuthenticationException e) {
            // 处理token校验异常，避免登录异常打印到控制台。
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.getWriter().print(JSONObject.toJSONString(SimpleResponseForm.error(e.getMessage())));
            return false;
        }
    }
}
