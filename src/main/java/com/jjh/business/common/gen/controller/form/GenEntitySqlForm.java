package com.jjh.business.common.gen.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 生成实体SQL 表单
 *
 * @author jjh
 * @date 2020/05/15
 **/
@ApiModel("生成实体SQL 表单")
@Data
public class GenEntitySqlForm {

    /** 类名全路径 */
    @NotBlank(message = "类名全路径不能为空")
    @ApiModelProperty(value = "类名全路径（类的包名+类名）", example = "com.jjh.business.system.user.model.SysUser")
    private String classPackageName;

    /** 是否包含基础字段 */
    @ApiModelProperty(value = "是否包含基础字段", example = "true")
    private Boolean baseColumnFlag;

}
