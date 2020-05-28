package com.jjh.business.system.tenant.service;

import com.jjh.business.system.tenant.model.SysApplication;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 子系统 服务层
 *
 * @author jjh
 * @date 2020/02/18
 */
public interface SysApplicationService {

    /**
     * 查询子系统列表
     *
     * @param form 查询条件
     * @return 子系统集合
     */
    List<SysApplication> list(PageRequestForm<SysApplication> form);

    /**
     * 新增子系统对象
     *
     * @param entity 待新增对象
     * @return 子系统对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysApplication add(SysApplication entity);


    /**
     * 更新子系统对象
     *
     * @param entity 待更新对象
     * @return 子系统对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysApplication update(SysApplication entity);


    /**
     * 删除子系统对象
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
    void importData(List<SysApplication> list, boolean updateSupport);

    /**
     * 查询所有子系统
     * @return
     */
    List<SysApplication> queryAll();
}
