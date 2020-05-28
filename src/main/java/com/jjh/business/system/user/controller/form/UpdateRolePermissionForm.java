package com.jjh.business.system.user.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新角色权限关联 表单
 *
 * @author jjh
 * @date 2019/12/3
 **/
@ApiModel("更新角色权限关联表单")
@Data
public class UpdateRolePermissionForm {

    /** 角色ID*/
    @NotBlank(message = "角色ID不能为空")
    @ApiModelProperty("角色ID")
    private String roleId;

    /** 原有权限ID*/
    @ApiModelProperty("原有权限ID")
    private String permissionIds;

    /** 新权限ID*/
    @ApiModelProperty("新权限ID")
    private String lastPermissionIds;

}
