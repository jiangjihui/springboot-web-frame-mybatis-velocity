package com.common.web.form;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单分页查询类
 * @author jjh
 */
public class PageRequestForm {

    /** 页码 */
    private int pageNum;
    /** 每页大小 */
    private int pageSize;
    /** 排序字段 */
    private String orderColumn;
    /** 排序方式：asc 升序/desc 降序 */
    private String sort;

    /** 筛选（过滤）参数 */
    private Map<String,Object> params;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderColumn() {
        return orderColumn;
    }

    public void setOrderColumn(String orderColumn) {
        this.orderColumn = orderColumn;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrderBy() {
        if (StringUtils.isEmpty(orderColumn)) {
            return "";
        }
        return orderColumn + " " + sort;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            return new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
