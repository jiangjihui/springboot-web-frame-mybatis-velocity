package com.jjh.business.system.user.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新用户角色关联 表单
 *
 * @author jjh
 * @date 2019/12/3
 **/
@ApiModel("更新用户角色关联表单")
@Data
public class UpdateUserRoleForm {

    /** 用户ID*/
    @NotBlank(message = "用户ID不能为空")
    @ApiModelProperty("用户ID")
    private String userId;

    /** 角色ID*/
    @ApiModelProperty("角色ID")
    private String roleIds;

}
