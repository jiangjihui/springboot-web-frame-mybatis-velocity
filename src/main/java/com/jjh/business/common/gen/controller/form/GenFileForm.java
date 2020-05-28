package com.jjh.business.common.gen.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 生成指定文件 表单
 * 自定义模板和参数进行指定文件生成
 *
 * @author jjh
 * @date 2019/12/26
 **/
@ApiModel("生成指定路径代码 表单")
@Data
public class GenFileForm {

    /** 模板文件路径*/
    @NotBlank(message = "模板文件路径不能为空")
    @ApiModelProperty(value = "模板文件路径", example = "vm/java/Repository.java.vm")
    private String templateFilePath;

    /** 目标文件路径*/
    @NotBlank(message = "目标文件路径不能为空")
    @ApiModelProperty(value = "目标文件路径", example = "D:\\gg\\Repository.java")
    private String targetFilePath;

    /** 模板参数*/
    @ApiModelProperty("模板参数")
    private Map<String,Object> params;

}
