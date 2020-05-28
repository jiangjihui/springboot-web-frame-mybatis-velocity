package com.jjh.business.system.user.service;

import com.jjh.business.system.user.controller.form.QueryRoleForm;
import com.jjh.business.system.user.controller.form.UpdateUserRoleForm;
import com.jjh.business.system.user.model.SysRole;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色实体类 服务层
 *
 * @author jjh
 * @date 2019/11/20
 */
public interface SysRoleService {

    /**
     * 查询角色实体类列表
     *
     * @param form 查询条件
     * @return 角色实体类集合
     */
    public List<SysRole> list(PageRequestForm<QueryRoleForm> form);

    /**
     * 新增角色实体类对象
     *
     * @param entity 待新增对象
     * @return 角色实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    public SysRole add(SysRole entity);


    /**
     * 更新角色实体类对象
     *
     * @param entity 待更新对象
     * @return 角色实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    public SysRole update(SysRole entity);


    /**
     * 删除角色实体类对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean del(String ids);

    /**
     * 更新用户角色关联
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    void updateUserRole(UpdateUserRoleForm form);

    /**
     * 查询用户角色关联
     * @param userId    用户ID
     * @return  角色ID数组
     */
    List<String> queryUserRole(String userId);

    /**
     * 查询所有角色
     * @return  角色列表
     */
    List<SysRole> queryAll();
}
