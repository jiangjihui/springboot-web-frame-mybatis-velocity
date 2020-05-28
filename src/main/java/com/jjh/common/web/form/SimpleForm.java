package com.jjh.common.web.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 查询表单
 *
 * @author jjh
 * @date 2019/6/1
 **/
@ApiModel("查询表单")
public class SimpleForm {

    @NotBlank(message = "ID不能为空")
    @ApiModelProperty(value = "ID", required = true)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
