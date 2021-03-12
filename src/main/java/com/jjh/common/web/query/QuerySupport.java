package com.jjh.common.web.query;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;

/**
 * 增强查询
 *
 * @author jjh
 * @date 2021/03/11
 **/
public class QuerySupport {

    private static final Logger logger= LoggerFactory.getLogger(QuerySupport.class);

    /**
     * 自定义查询注解处理
     * @param filter  条件类
     * @param queryWrapper  查询条件
     */
    public static void queryWrapper(Object filter, QueryWrapper queryWrapper) {
        if (filter == null) {
            return;
        }
        Class<?> clazz = filter.getClass();
        Field[] fields = ClassUtil.getDeclaredFields(clazz);
        for (Field field : fields) {
            QueryCondition condition = field.getAnnotation(QueryCondition.class);
            if (condition == null) {
                continue;
            }
            String column = condition.column();
            if (StrUtil.isBlank(column)) {
                column = field.getName();
            }
            // 处理驼峰命名转下划线
            Environment environment = SpringUtil.getBean(Environment.class);
            Boolean isUnderlineCase = environment.getProperty("mybatis-plus.configuration.map-underscore-to-camel-case", Boolean.class);
            if (isUnderlineCase != null && Boolean.TRUE.equals(isUnderlineCase)) {
                StrUtil.toUnderlineCase(column);
            }
            // 处理private属性
            field.setAccessible(true);
            Object value = null;
            // 获取字段值
            try {
                value = field.get(filter);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.info("获取查询值异常", e);
            }
            if (value == null) {
                continue;
            }
            // 处理空值
            if (value instanceof String) {
                if (StrUtil.isBlank((String) value)) {
                    continue;
                }
            }
            switch (condition.func()) {
                case equal:
                    queryWrapper.eq(column, value);
                    break;
                case ge:
                    queryWrapper.ge(column, value);
                    break;
                case gt:
                    queryWrapper.gt(column, value);
                    break;
                case in:
                    queryWrapper.in(column, value);
                    break;
                case le:
                    queryWrapper.le(column, value);
                    break;
                case lt:
                    queryWrapper.lt(column, value);
                    break;
                case like:
                    queryWrapper.like(column, value);
                    break;
                case notIn:
                    queryWrapper.notIn(column, value);
                    break;
                case notLike:
                    queryWrapper.notLike(column, value);
                    break;
            }
        }
    }

}
