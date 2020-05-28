package com.jjh.business.system.support.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统字典查询表单
 *
 * @author jjh
 * @date 2020/4/8
 **/
@ApiModel("系统字典查询表单")
@Data
public class SysDictTypeQueryForm {

    @ApiModelProperty("字典分类名称")
    private String name_WithLikeAll;

    @ApiModelProperty("字典分类编号")
    private String code;

}
