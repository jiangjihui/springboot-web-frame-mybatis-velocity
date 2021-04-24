package com.jjh.business.demo.article.service;

import com.jjh.business.demo.article.controller.form.ArticleQueryListForm;
import com.jjh.business.demo.article.model.Article;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 文章 服务层
 *
 * @author jjh
 * @date 2020/02/16
 */
public interface ArticleService {

    /**
     * 查询文章列表
     *
     * @param form 查询条件
     * @return 文章集合
     */
    List<Article> list(PageRequestForm<ArticleQueryListForm> form);

    /**
     * 新增文章对象
     *
     * @param entity 待新增对象
     * @return 文章对象
     */
    @Transactional(rollbackFor = Exception.class)
    Article add(Article entity);


    /**
     * 更新文章对象
     *
     * @param entity 待更新对象
     * @return 文章对象
     */
    @Transactional(rollbackFor = Exception.class)
    Article update(Article entity);


    /**
     * 删除文章对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

}
