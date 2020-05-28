package com.jjh.business.system.user.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码 表单
 */
@ApiModel("修改密码表单")
@Data
public class ResetPasswordForm {

    @NotBlank(message = "ID不能为空")
    @ApiModelProperty("用户编号")
    private String id;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String newPassword;

}
