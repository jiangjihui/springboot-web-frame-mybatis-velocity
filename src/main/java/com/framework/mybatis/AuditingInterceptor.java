package com.framework.mybatis;

import com.framework.mybatis.annotation.CreatedBy;
import com.framework.mybatis.annotation.CreatedTime;
import com.framework.mybatis.annotation.UpdateBy;
import com.framework.mybatis.annotation.UpdateTime;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 审计接口，自动注入用户id以及自动获取注入更新时间和创建时间
 * https://my.oschina.net/stategrace/blog/347272
 * <p>
 * MyBatis的Interceptor
 * https://www.jianshu.com/p/8c360568eab2
 *
 * Springboot2（22）Mybatis拦截器实现
 * https://blog.csdn.net/cowbin2012/article/details/85256360
 */
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuditingInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
//        Field[] fields = parameter.getClass().getDeclaredFields();
        List<Field> fields = getAllField(parameter.getClass());
        Date currentDate = new Date();
        String userId = "jjh";

        if (SqlCommandType.UPDATE == sqlCommandType) {
            for (Field field : fields) {
                if (AnnotationUtils.getAnnotation(field, UpdateBy.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, userId);
                    field.setAccessible(false);
                }
                if (AnnotationUtils.getAnnotation(field, UpdateTime.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, currentDate);
                    field.setAccessible(false);
                }
            }
        } else if (SqlCommandType.INSERT == sqlCommandType) {
            for (Field field : fields) {
                if (AnnotationUtils.getAnnotation(field, CreatedBy.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, userId);
                    field.setAccessible(false);
                }
                if (AnnotationUtils.getAnnotation(field, CreatedTime.class) != null) {
                    field.setAccessible(true);
                    field.set(parameter, currentDate);
                    field.setAccessible(false);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object o) {
        if (o instanceof Executor) {
            return Plugin.wrap(o, this);
        }
        return o;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    //获取所有字段
    public List<Field> getAllField(Class clazz) {
        ArrayList<Field> fields = new ArrayList<>();
        Class tempClass = clazz;
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        while (tempClass != null) {
            tempClass = tempClass.getSuperclass();
            if (tempClass != null) {
                fields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            }
        }
        return fields;
    }

}
