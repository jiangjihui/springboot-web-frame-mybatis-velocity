package com.jjh.business.system.user.service;

import com.jjh.business.system.user.controller.form.UpdateRolePermissionForm;
import com.jjh.business.system.user.model.SysPermission;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限实体类 服务层
 *
 * @author jjh
 * @date 2019/11/20
 */
public interface SysPermissionService {

    /**
     * 查询权限实体类列表
     *
     * @param form 查询条件
     * @return 权限实体类集合
     */
    List<SysPermission> list(PageRequestForm<SysPermission> form);

    /**
     * 新增权限实体类对象
     *
     * @param entity 待新增对象
     * @return 权限实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysPermission add(SysPermission entity);


    /**
     * 更新权限实体类对象
     *
     * @param entity 待更新对象
     * @return 权限实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysPermission update(SysPermission entity);


    /**
     * 删除权限实体类对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

    /**
     * 更新角色权限关联
     * @param form
     */
    void updateRolePermission(UpdateRolePermissionForm form);

    /**
     * 查询角色权限关联
     * @param roleId    角色ID
     * @return 权限ID数组
     */
    List<String> queryRolePermission(String roleId);

    /**
     * 查询所有权限（树图关联）
     * @return  权限列表
     */
    List<SysPermission> queryTreeList();

    /**
     * 查询所有可选权限（树图关联）
     * @return  权限列表
     */
    List<SysPermission> selectTreeList();

    /**
     * 查询用户菜单
     * @return
     */
    List<SysPermission> queryUserMenu();
}
