package com.jjh.business.system.support.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.support.mapper.SysCategoryMapper;
import com.jjh.business.system.support.model.SysCategory;
import com.jjh.business.system.support.service.SysCategoryService;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 分类字典 服务层实现
 *
 * @author jjh
 * @date 2019/12/12
 */
@Service
public class SysCategoryServiceImpl implements SysCategoryService {

    @Resource
    private SysCategoryMapper sysCategoryMapper;


    /**
     * 查询分类字典列表
     *
     * @param form 查询条件
     * @return 分类字典集合
     */
    @Override
    public List<SysCategory> list(PageRequestForm<SysCategory> form) {
        return sysCategoryMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增分类字典对象
     *
     * @param entity 待新增对象
     * @return 分类字典对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysCategory add(SysCategory entity) {
        entity.setId(IdGenerateHelper.nextId());
        if (StrUtil.isBlank(entity.getParentId())) {
            entity.setParentId("");
        }
        sysCategoryMapper.insert(entity);
        return entity;
    }

    /**
     * 更新分类字典对象
     *
     * @param entity 待更新对象
     * @return 分类字典对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysCategory update(SysCategory entity) {
        SysCategory oldEntity = sysCategoryMapper.selectById(entity.getId());
        if (Objects.isNull(oldEntity)) {
            throw new BusinessException("对象不存在，请检查");
        }
        if (StrUtil.isBlank(entity.getParentId())) {
            entity.setParentId("");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysCategoryMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除分类字典对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        sysCategoryMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

    /**
     * 导入数据
     *
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(List<SysCategory> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysCategory entity : list) {
            SysCategory oldEntity = sysCategoryMapper.selectById(entity.getId());
            if (oldEntity != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
                    sysCategoryMapper.updateById(oldEntity);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。");
                }
            }
            else {
                entity.setId(IdGenerateHelper.nextId());
                sysCategoryMapper.insert(entity);
            }
        }
    }

    /**
     * 顶级分类字典列表
     *
     * @param form 查询条件
     */
    @Override
    public List<SysCategory> rootList(PageRequestForm<SysCategory> form) {
        SysCategory filter = form.getFilter();
        if (filter == null) {
            filter = new SysCategory();
            form.setFilter(filter);
        }
        filter.setParentId("");
        return sysCategoryMapper.selectList(form.pageWrapper());
    }

    /**
     * 子分类字典列表
     *
     * @param parentId 查询条件
     * @return
     */
    @Override
    public List<SysCategory> childrenList(String parentId) {
        return sysCategoryMapper.selectList(Wrappers.<SysCategory>lambdaQuery().eq(SysCategory::getParentId, parentId));
    }

    /**
     * 查找分类字典
     *
     * @param code  分类字典编码
     * @return
     */
    @Override
    public SysCategory getByCode(String code) {
        return sysCategoryMapper.selectOne(Wrappers.<SysCategory>lambdaQuery().eq(SysCategory::getCode, code));
    }

    /**
     * 获取分类字典树结构
     * @param code  分类字典编码
     * @return
     */
    @Override
    public SysCategory loadTree(String code) {
        List<SysCategory> categoryList = sysCategoryMapper.selectList(Wrappers.<SysCategory>lambdaQuery().likeLeft(SysCategory::getCode, code));
        SysCategory root = null;
        Map<String, SysCategory> categoryMap = new HashMap<>();
        for (SysCategory category : categoryList) {
            if (code.equals(category.getCode())) {
                root = category;
            }
            categoryMap.put(category.getId(), category);
        }
        for (SysCategory category : categoryList) {
            SysCategory parentCategory = categoryMap.get(category.getParentId());
            if (parentCategory != null) {
                List<SysCategory> children = parentCategory.getChildren();
                if (children == null) {
                    children = new LinkedList<>();
                    parentCategory.setChildren(children);
                }
                children.add(category);
            }
        }
        return root;
    }

}
