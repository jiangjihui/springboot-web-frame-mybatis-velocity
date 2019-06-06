package com.common.web.controller;

import com.common.util.web.PageSupport;
import com.common.web.form.PageRequestForm;
import com.common.web.form.PageResponseForm;
import com.common.web.form.SimpleResponseForm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * web层通用数据处理
 *
 * @author jjh
 * @date 2019/6/1
 **/
public class BaseController {


    /**
     * 返回成功结果
     * @param result 结果
     */
    public Object success(Object result){
        return SimpleResponseForm.success(result);
    }

    /**
     * 返回成功结果
     */
    public Object success(){
        return this.success("success");
    }

    /**
     * 返回失败结果
     * @param message 失败信息
     */
    public Object error(String message){
        return SimpleResponseForm.error(message);
    }

    public Object page(List result) {
        PageResponseForm form = new PageResponseForm(new PageInfo(result).getTotal(), result);
        return SimpleResponseForm.success(form);
    }


    /**
     * 开启分页查询
     */
    public void startPage() {
        PageRequestForm pageDomain = PageSupport.buildPageRequest();
        if (pageDomain.getPageSize() != 0) {
//            PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
            PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize(), pageDomain.getOrderBy());
        }

    }

    /**
     * 开启分页查询
     */
    public void startPage(PageRequestForm form) {
        if (form.getPageSize() != 0) {
            PageHelper.startPage(form.getPageNum(), form.getPageSize(), form.getOrderBy());
        }

    }

}
