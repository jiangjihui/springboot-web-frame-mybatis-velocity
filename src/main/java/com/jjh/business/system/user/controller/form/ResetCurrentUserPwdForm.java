package com.jjh.business.system.user.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wj
 * @version 1.0
 * @date 2020/1/6
 */
@ApiModel("修改当前用户密码表单")
@Data
public class ResetCurrentUserPwdForm {
    @NotBlank(message = "旧密码不能为空")
    @ApiModelProperty("旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String newPassword;
}
