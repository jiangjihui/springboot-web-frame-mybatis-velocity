package com.jjh.business.system.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;

import static com.jjh.business.system.user.model.SysUserRoleMapping.TABLE_NAME;

@TableName(TABLE_NAME)
public class SysUserRoleMapping extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_user_role_mapping";

    /** 用户编号 */
    private String userId;

    /** 角色编号 */
    private String roleId;

    @TableField(exist = false)
    private SysUser sysUser;

    @TableField(exist = false)
    private SysRole role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }
}
