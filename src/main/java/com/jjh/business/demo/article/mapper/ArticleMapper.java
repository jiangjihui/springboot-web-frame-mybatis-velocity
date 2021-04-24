package com.jjh.business.demo.article.mapper;

import com.jjh.business.demo.article.model.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 文章 数据层
 * 
 * @author jjh
 * @date 2020/02/16
 */
public interface ArticleMapper extends BaseMapper<Article> {

    int delete(String[] ids);

}