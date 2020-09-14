package com.jjh.common.web.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
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
    public <T> SimpleResponseForm<T> success(T result){
        return new SimpleResponseForm<>(result);
    }

    /**
     * 返回成功结果
     */
    public <T> SimpleResponseForm<T> success(){
        return new SimpleResponseForm<>();
    }

    /**
     * 返回失败结果
     * @param message 失败信息
     */
    public SimpleResponseForm error(String message){
        return SimpleResponseForm.error(message);
    }

    /**
     * 返回分页数据对象
     * @param result    分页数据
     * @param <T>   数据对象
     * @return
     */
    public <T> SimpleResponseForm<PageResponseForm<T>> page(Page<T> result) {
        PageResponseForm form = new PageResponseForm<T>(result.getTotalElements(), result.getContent());
        return new SimpleResponseForm<PageResponseForm<T>>(form);
    }

    /**
     * 返回分页数据对象
     * @param pageReqForm   分页查询条件
     * @param result    数据列表
     * @param <T>   数据对象
     * @return
     */
    public <T> SimpleResponseForm<PageResponseForm<T>> page(PageRequestForm pageReqForm, List<T> result) {
        if (result == null) {
            pageReqForm.setTotal(0L);
            result = new ArrayList<>();
        }
        Long total = pageReqForm.getTotal();
        if (total == null) {
            total = new PageInfo(result).getTotal();
        }
        PageResponseForm form = new PageResponseForm<T>(total, result);
        return new SimpleResponseForm<PageResponseForm<T>>(form);
    }

    /**
     * 页面跳转
     */
    public static String redirect(String url)
    {
        return StrUtil.format("redirect:{}", url);
    }
}
