package com.jjh.common.web.form;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jjh.framework.jpa.SpecificSuffix;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageHelper;
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
    @ApiModelProperty(value = "页码", required = true)
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
        return queryWrapper;
    }

    /**
     * 查询条件封装
     * @param clazz 需要映射到的数据库类
     * @return
     */
    public QueryWrapper pageQueryWrapper(Class clazz){
        PageHelper.startPage(pageNum + 1, pageSize);

        if (clazz == null || filter == null) {
            return this.queryWrapper();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        // 查询类映射
        queryWrapper.setEntity(BeanUtil.copyProperties(filter, clazz));
        // 自定义前缀查询包装
        specificSuffixQueryWrapper(queryWrapper);

        return queryWrapper;
    }

    /**
     * 自定义前缀查询包装
     * @param queryWrapper
     */
    private void specificSuffixQueryWrapper(QueryWrapper queryWrapper) {
        // 自定义查询字段处理
        String paramKey = null;
        Map<String, Object> paramMap = BeanUtil.beanToMap(filter);
        if (paramMap == null) {
            return;
        }
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            // 表达式判断
            if (entry.getKey().contains("_With")) {
                Object entryValue = entry.getValue();
                if (entryValue == null){
                    continue;
                }

                if (entry.getKey().endsWith(SpecificSuffix.LIKE)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.LIKE, "");
                    queryWrapper.likeRight(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.LIKE_ALL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.LIKE_ALL, "");
                    queryWrapper.like(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.IS_NULL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.IS_NULL, "");
                    queryWrapper.isNull(paramKey);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.IN)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.IN, "");
                    queryWrapper.in(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.GREAT_EQUAL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.GREAT_EQUAL, "");
                    queryWrapper.ge(paramKey, entryValue);
                }
                else if (entry.getKey().endsWith(SpecificSuffix.LESS_EQUAL)) {
                    paramKey = entry.getKey().replace(SpecificSuffix.LESS_EQUAL, "");
                    queryWrapper.le(paramKey, entryValue);
                }
            }
        }
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
        PageHelper.startPage(pageNum + 1, pageSize);
        return wrapper;
    }
}
