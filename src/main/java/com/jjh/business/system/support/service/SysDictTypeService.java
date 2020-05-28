package com.jjh.business.system.support.service;

import com.jjh.business.system.support.controller.form.SysDictTypeQueryForm;
import com.jjh.business.system.support.model.SysDictType;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典类型 服务层
 *
 * @author jjh
 * @date 2019/12/09
 */
public interface SysDictTypeService {

    /**
     * 查询字典类型列表
     *
     * @param form 查询条件
     * @return 字典类型集合
     */
    List<SysDictType> list(PageRequestForm<SysDictTypeQueryForm> form);

    /**
     * 新增字典类型对象
     *
     * @param entity 待新增对象
     * @return 字典类型对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysDictType add(SysDictType entity);


    /**
     * 更新字典类型对象
     *
     * @param entity 待更新对象
     * @return 字典类型对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysDictType update(SysDictType entity);


    /**
     * 删除字典类型对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

    /**
     * 导入数据
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    void importData(List<SysDictType> list, boolean updateSupport);

    /**
     * 查询所有字典类型以及其对应的字典值
     *
     * @return 字典类型集合
     */
    List<SysDictType> listAllTypeAndData();
}
