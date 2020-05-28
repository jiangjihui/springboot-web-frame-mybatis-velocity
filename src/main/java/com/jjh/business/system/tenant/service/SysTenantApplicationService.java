package com.jjh.business.system.tenant.service;

import com.jjh.business.system.tenant.model.SysTenantApplication;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户子系统关联 服务层
 *
 * @author jjh
 * @date 2020/02/18
 */
public interface SysTenantApplicationService {

    /**
     * 查询租户子系统关联列表
     *
     * @param form 查询条件
     * @return 租户子系统关联集合
     */
    List<SysTenantApplication> list(PageRequestForm<SysTenantApplication> form);

    /**
     * 新增租户子系统关联对象
     *
     * @param entity 待新增对象
     * @return 租户子系统关联对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysTenantApplication add(SysTenantApplication entity);


    /**
     * 更新租户子系统关联对象
     *
     * @param entity 待更新对象
     * @return 租户子系统关联对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysTenantApplication update(SysTenantApplication entity);


    /**
     * 删除租户子系统关联对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

}
