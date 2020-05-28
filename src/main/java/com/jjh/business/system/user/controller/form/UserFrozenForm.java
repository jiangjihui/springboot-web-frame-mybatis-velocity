package com.jjh.business.system.user.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户冻结/解冻 表单
 */
@ApiModel("用户冻结/解冻 表单")
@Data
public class UserFrozenForm {

    @NotBlank(message = "ID不能为空")
    @ApiModelProperty("用户编号")
    private String id;

    @NotBlank(message = "（冻结）状态不能为空")
    @ApiModelProperty("（冻结）状态")
    private Integer status;

}
