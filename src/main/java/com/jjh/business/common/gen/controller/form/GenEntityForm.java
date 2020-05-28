package com.jjh.business.common.gen.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 生成指定路径下的代码 表单
 *
 * @author jjh
 * @date 2019/11/13
 **/
@ApiModel("生成指定路径代码 表单")
@Data
public class GenEntityForm {

    /** 所属分类*/
    @ApiModelProperty("所属分类")
    private String moduleName;

    /** 实体类名*/
    @ApiModelProperty("实体类名")
    private String className;

    /** 实体类描述*/
    @ApiModelProperty("实体类描述")
    private String comment;

    /** 包名（比如：com.business.demo）*/
    @ApiModelProperty("包名")
    private String packageName;

    /** 作者（比如：jjh）*/
    @ApiModelProperty("作者")
    private String author;

    /** 目的文件路径*/
    @ApiModelProperty("目的文件路径")
    private String targetPath;

    /** 需要生成的目标文件类型*/
    @ApiModelProperty(value = "需要生成的目标文件类型（为空时默认生成全部目标文件类型）", example = "Service,Controller")
    private String targetFile;

    /** 是否生成导入导出代码*/
    @ApiModelProperty("是否生成导入导出代码")
    private Boolean importAndExport;
}
