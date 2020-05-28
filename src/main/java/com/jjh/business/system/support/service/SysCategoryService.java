package com.jjh.business.system.support.service;

import com.jjh.business.system.support.model.SysCategory;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类字典 服务层
 *
 * @author jjh
 * @date 2019/12/12
 */
public interface SysCategoryService {

    /**
     * 查询分类字典列表
     *
     * @param form 查询条件
     * @return 分类字典集合
     */
    List<SysCategory> list(PageRequestForm<SysCategory> form);

    /**
     * 新增分类字典对象
     *
     * @param entity 待新增对象
     * @return 分类字典对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysCategory add(SysCategory entity);


    /**
     * 更新分类字典对象
     *
     * @param entity 待更新对象
     * @return 分类字典对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysCategory update(SysCategory entity);


    /**
     * 删除分类字典对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

    /**
     * 导入数据
     *
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    void importData(List<SysCategory> list, boolean updateSupport);

    /**
     * 顶级分类字典列表
     *
     * @param form 查询条件
     */
    List<SysCategory> rootList(PageRequestForm<SysCategory> form);

    /**
     * 子分类字典列表
     *
     * @param parentId 父分类ID
     */
    List<SysCategory> childrenList(String parentId);

    /**
     * 查找分类字典
     *
     * @param code  分类字典编码
     * @return
     */
    SysCategory getByCode(String code);


    /**
     * 获取分类字典树结构
     * @param code  分类字典编码
     * @return
     */
    SysCategory loadTree(String code);
}
