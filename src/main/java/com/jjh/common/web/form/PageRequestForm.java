package com.jjh.common.web.form;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageHelper;
import com.jjh.common.web.query.QuerySupport;
import com.jjh.framework.jpa.SpecificSuffix;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * 简单分页查询类
 */
@Data
@ApiModel("分页请求查询表单")
public class PageRequestForm<T> {

    /** 页码（从0开始） */
    @ApiModelProperty(value = "页码", example = "1", required = true)
    private Integer pageNum;

    /** 每页大小 */
    @ApiModelProperty(value = "每页大小", example = "10", required = true)
    private Integer pageSize;

    /** 总数 */
//    @ApiModelProperty("总数")
    @JsonIgnore
    private Long total;

    /** 排序 */
//    @JsonIgnore
    @ApiModelProperty("排序")
    private Map<String, String> sort;

    /** 是否导出全部数据（导出选项） */
    @ApiModelProperty(value = "是否导出全部数据（导出excel时使用）", example = "false")
    private Boolean exportAll;

    /** 查询实体类*/
    private T filter;

    /**
     * 查询条件封装
     * @return
     */
    public QueryWrapper queryWrapper(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.setEntity(filter);
        sortQueryWrapper(queryWrapper);
        return queryWrapper;
    }

    /**
     * 排序包装
     * @return
     */
    public QueryWrapper sortQueryWrapper(QueryWrapper queryWrapper){
        if (sort != null && !sort.isEmpty()) {
            for (Map.Entry<String, String> sortEntity : sort.entrySet()) {
                String columnName = sortEntity.getKey();
                if (StrUtil.isBlank(columnName)) {
                    continue;
                }
                String direction = sortEntity.getValue().trim();
                if ("desc".equals(direction) || "-1".equals(direction)) {
                    queryWrapper.orderByDesc(columnName);
                }
                else {
                    queryWrapper.orderByAsc(columnName);
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 查询条件封装
     * @param clazz 需要映射到的数据库类
     * @return
     */
    public QueryWrapper pageQueryWrapper(Class clazz){
        PageHelper.startPage(pageNum, pageSize);

        if (clazz == null || filter == null) {
            return this.queryWrapper();
        }
        // 自定义前缀查询包装
        QueryWrapper queryWrapper = specificSuffixQueryWrapper(filter, clazz);
        return sortQueryWrapper(queryWrapper);
    }

    /**
     * 自定义前缀查询包装
     * @param filterObject  条件类
     * @param clazz  映射类类型
     */
    public static QueryWrapper specificSuffixQueryWrapper(Object filterObject, Class clazz) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 查询类映射
        queryWrapper.setEntity(BeanUtil.copyProperties(filterObject, clazz));
        // 自定义查询字段处理
        String paramKey = null;
        Map<String, Object> paramMap = BeanUtil.beanToMap(filterObject);
        if (paramMap == null) {
            return null;
        }
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            // 表达式判断
            if (entry.getKey().contains("_With")) {
                Object entryValue = entry.getValue();
                if (entryValue == null){
                    continue;
                }
                if (entryValue instanceof String) {
                    if (StrUtil.isBlank((String) entryValue)) {
                        continue;
                    }
                }

                if (entry.getKey().endsWith(SpecificSuffix.LIKE)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.LIKE, "");
                    paramKey = StrUtil.toUnderlineCase(paramKey);
                    queryWrapper.likeRight(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.LIKE_ALL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.LIKE_ALL, "");
                    paramKey = StrUtil.toUnderlineCase(paramKey);
                    queryWrapper.like(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.IS_NULL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.IS_NULL, "");
                    paramKey = StrUtil.toUnderlineCase(paramKey);
                    queryWrapper.isNull(paramKey);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.IN)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.IN, "");
                    paramKey = StrUtil.toUnderlineCase(paramKey);
                    queryWrapper.in(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.GREAT_EQUAL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.GREAT_EQUAL, "");
                    paramKey = StrUtil.toUnderlineCase(paramKey);
                    queryWrapper.ge(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.LESS_EQUAL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.LESS_EQUAL, "");
                    paramKey = StrUtil.toUnderlineCase(paramKey);
                    queryWrapper.le(paramKey, entryValue);
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 查询条件封装
     * @param clazz 需要映射到的数据库类
     * @return
     */
    public QueryWrapper pageWrapper(Class clazz){
        return this.pageQueryWrapper(clazz);
    }

    /**
     * 查询条件封装
     * @return
     */
    public QueryWrapper pageWrapper(){
        return this.pageQueryWrapper(null);
    }

    /**
     * 查询条件封装
     * @return
     */
    public Wrapper pageWrapper(Wrapper wrapper){
        PageHelper.startPage(pageNum, pageSize);
        return wrapper;
    }

    /**
     * 查询条件封装
     * @return
     */
    public Wrapper pageWrapperWithSort(QueryWrapper wrapper){
        PageHelper.startPage(pageNum, pageSize);
        return sortQueryWrapper(wrapper);
    }

    /**
     * 处理@QueryCondition注解查询
     * @return
     */
    public Wrapper pageWrapperQuerySupport(){
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        // 处理注解查询
        QuerySupport.queryWrapper(filter, queryWrapper);
        return queryWrapper;
    }
}
