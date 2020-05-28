package com.jjh.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * Pojo操作类
 * BeanUtils.copyProperties在拷贝属性时忽略空值
 * https://blog.csdn.net/u010186511/article/details/51595720
 */
public class PojoUtils {

    /**
     * 获取null属性
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 获取空字段（包括空字符串）
     * @param source
     * @return
     */
    public static String[] getBlankPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            } else if ((srcValue instanceof String) && "".equals((String) srcValue)) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 复制非空属性
     * @param src   源对象
     * @param target    目的对象
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 复制非空字符串属性（包含非空属性）
     * @param src   源对象
     * @param target    目的对象
     */
    public static void copyPropertiesIgnoreBlank(Object src, Object target){
        BeanUtils.copyProperties(src, target, getBlankPropertyNames(src));
    }


}
