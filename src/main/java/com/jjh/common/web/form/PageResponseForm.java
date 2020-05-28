package com.jjh.common.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 返回分页数据
 *
 * @author jjh
 * @date 2019/6/1
 **/
@ApiModel("分页对象")
public class PageResponseForm<T> {

    /*总记录数*/
    @ApiModelProperty("总记录数")
    private Long total;

    /*列表数据*/
    @ApiModelProperty("列表数据")
    private List<T> rows;

    public PageResponseForm() {
    }

    public PageResponseForm(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
