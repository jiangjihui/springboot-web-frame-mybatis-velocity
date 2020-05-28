package com.jjh.business.system.user.service;


import com.jjh.business.system.user.controller.form.QueryUserForm;
import com.jjh.business.system.user.controller.form.ResetCurrentUserPwdForm;
import com.jjh.business.system.user.controller.form.ResetPasswordForm;
import com.jjh.business.system.user.controller.form.UserFrozenForm;
import com.jjh.business.system.user.model.SysUser;
import com.jjh.common.web.form.PageRequestForm;

import java.util.List;

/**
 * 用户信息 服务
 *
 * @author jjh
 * @date 2019/9/20
 */
public interface SysUserService {

    /**
     * 用户列表
     * @param form 分页请求表单
     * @return
     */
    List<SysUser> list(PageRequestForm<QueryUserForm> form);

    /**
     * 根据用户名查找用户
     * @param username  用户名
     * @return
     */
    SysUser findByUsername(String username);

    /**
     * 根据用户ID查找用户
     * @param id    用户ID
     * @return
     */
    SysUser findById(String id);

    /**
     * 添加用户
     * @param sysUser 用户信息
     * @return 用户信息
     */
    SysUser add(SysUser sysUser);

    /**
     * 更新用户
     * @param entity 用户信息
     * @return 用户信息
     */
    SysUser update(SysUser entity);

    /**
     * 删除用户
     * @param ids 待删除的ID数组
     */
    void delete(String ids);

    /**
     *  更新用户密码
     * @param form 密码表单
     */
    SysUser resetPassword(ResetPasswordForm form);

    /**
     *  获取角色Code
     * @param id    用户ID
     */
    List<String> listSysRoleCode(String id);

    /**
     *  获取权限Code
     * @param id    用户ID
     */
    List<String> listSysPermissionCode(String id);

    /**
     * 冻结/解冻
     * @param list
     * @return
     */
    void frozen(List<UserFrozenForm> list);

    /**
     * 导入数据
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    void importData(List<SysUser> list, Boolean updateSupport);

    /**
     * 获取用户信息
     * @param id    用户ID
     * @return
     */
    SysUser info(String id);

    /**
     *  更新当前用户密码
     * @param form 密码表单
     */
    SysUser resetCurrentUserPwd(ResetCurrentUserPwdForm form);

    /**
     * 根据用户名称获取用户信息
     * @param name  用户名称
     * @return
     */
    List<SysUser> findByName(String name);
}
