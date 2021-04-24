package com.jjh.business.demo.article.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jjh.business.demo.article.controller.form.ArticleQueryListForm;
import com.jjh.business.demo.article.mapper.ArticleMapper;
import com.jjh.business.demo.article.model.Article;
import com.jjh.business.demo.article.service.ArticleService;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文章 服务层实现
 *
 * @author jjh
 * @date 2020/02/16
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;


    /**
     * 查询文章列表
     *
     * @param form 查询条件
     * @return 文章集合
     */
    @Override
    public List<Article> list(PageRequestForm<ArticleQueryListForm> form) {
        return articleMapper.selectList(form.pageWrapperQuerySupport());
    }

    /**
     * 新增文章对象
     *
     * @param entity 待新增对象
     * @return 文章对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Article add(Article entity) {
        entity.setId(IdGenerateHelper.nextId());
        articleMapper.insert(entity);
        return entity;
    }

    /**
     * 更新文章对象
     *
     * @param entity 待更新对象
     * @return 文章对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Article update(Article entity) {
        Article oldEntity = articleMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        articleMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 删除文章对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        articleMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

}
