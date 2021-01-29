package com.jjh.business.common.gen.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 生成指定路径下的代码 表单
 *
 * 示例：
 * {"params":{"entityName":"Client","entityPackage":"client","columns":[{"fieldName":"code","filedComment":"单位编号","fieldType":"String"},{"fieldName":"name","filedComment":"客户名称","fieldType":"String","nullable":"N"}]},"targetFilePath":"D:/gg/ClientList.vue","templateFilePath":"vm/java/List.vue.vm"}
 *
 * @author jjh
 * @date 2019/11/20
 **/
@ApiModel("生成指定路径代码 表单")
@Data
public class GenTargetPathForm {

    /**
     * 代码绝对路径（包含到model包名）
     */
    @NotBlank(message = "代码绝对路径")
    @ApiModelProperty(value = "代码绝对路径（包含到model包名）", example = "D:\\Temp\\IDEA\\ecom_analysis_backend\\src\\main\\java\\com\\jjh\\business\\system\\support\\model")
    private String packagePath;

    /** 作者（比如：jjh）*/
    @NotBlank(message = "作者不能为空")
    @ApiModelProperty("作者")
    private String author;

    /** 目标文件类型*/
    @ApiModelProperty(value = "目标文件类型（,号分隔；为空时默认生成全部目标文件类型）", example = "Service,Controller")
    private String targetFile;

    /** 包含项*/
    @ApiModelProperty(value = "包含项（为空时默认包含全部）", example = "SysUse")
    private String includeEntity;

    /** 排除项*/
    @ApiModelProperty(value = "排除项（为空时则不存在排除项）", example = "UserInfo,RoInfo")
    private String excludeEntity;

    /** 是否生成导入导出代码*/
    @ApiModelProperty(value = "是否生成导入导出代码", example = "false")
    private Boolean importAndExport;
}
