package com.jjh.business.system.tenant.service;

import com.jjh.business.system.tenant.controller.form.DistributionApplicationForm;
import com.jjh.business.system.tenant.model.SysTenant;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户信息 服务层
 *
 * @author jjh
 * @date 2020/02/18
 */
public interface SysTenantService {

    /**
     * 查询租户信息列表
     *
     * @param form 查询条件
     * @return 租户信息集合
     */
    List<SysTenant> list(PageRequestForm<SysTenant> form);

    /**
     * 新增租户信息对象
     *
     * @param entity 待新增对象
     * @return 租户信息对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysTenant add(SysTenant entity);


    /**
     * 更新租户信息对象
     *
     * @param entity 待更新对象
     * @return 租户信息对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysTenant update(SysTenant entity);


    /**
     * 删除租户信息对象
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
    void importData(List<SysTenant> list, boolean updateSupport);

    /**
     * 租户分配子系统
     * @param form
     */
    void distributionApplication(DistributionApplicationForm form);

    /**
     * 获取租户子系统
     * @param tenantCode 租户编码
     * @return
     */
    List<String> findDistributionApplication(String tenantCode);
}
