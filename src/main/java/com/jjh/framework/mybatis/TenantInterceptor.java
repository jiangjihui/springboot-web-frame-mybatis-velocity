package com.jjh.framework.mybatis;

import cn.hutool.core.util.StrUtil;
import com.jjh.common.exception.BusinessException;
import com.jjh.framework.context.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

import java.sql.Connection;
import java.sql.SQLSyntaxErrorException;

/**
 * 租户切换
 * https://blog.csdn.net/qq_22200097/article/details/82942908
 * https://blog.csdn.net/qq_41988504/article/details/94734393
 *
 * @author jjh
 * @date 2020/2/28
 **/
@Slf4j
//@Component
//@Intercepts({
//        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
//})
public class TenantInterceptor implements Interceptor {

    public static final String DEFAULT_DATABASE = "jeecg_mybatis";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String tenant = TenantContextHolder.getTenant();
        if (StrUtil.isBlank(tenant)) {
            tenant = DEFAULT_DATABASE;
        }
        Connection connection = (Connection) invocation.getArgs()[0];
        // 切换当前数据库
        try {
            connection.createStatement().execute("use " + tenant);
        } catch (SQLSyntaxErrorException e) {
            log.info("切换数据库异常：database:"+ tenant);
            throw new BusinessException("租户编码异常，请检查：" + tenant);
        }

        // 继续执行调用链
        return invocation.proceed();
    }
}
