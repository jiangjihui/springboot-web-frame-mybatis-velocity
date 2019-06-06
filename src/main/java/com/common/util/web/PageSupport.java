package com.common.util.web;

import com.common.web.form.PageRequestForm;

public class PageSupport {

    public static PageRequestForm getPageDomain() {
        PageRequestForm pageDomain = new PageRequestForm();
        pageDomain.setPageNum(ServletUtils.getParameterToInt("pageNum"));
        pageDomain.setPageSize(ServletUtils.getParameterToInt("pageSize"));
        return pageDomain;
    }

    public static PageRequestForm buildPageRequest() {
        return getPageDomain();
    }
}
