package com.common.web.form;

/**
 * 返回分页数据
 *
 * @author jjh
 * @date 2019/6/1
 **/
public class PageResponseForm {

    /*总记录数*/
    private long total;
    /*列表数据*/
    private Object rows;

    public PageResponseForm() {
    }

    public PageResponseForm(long total, Object rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }
}
