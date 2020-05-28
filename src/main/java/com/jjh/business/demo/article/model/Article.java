package com.jjh.business.demo.article.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import com.jjh.framework.plugin.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static com.jjh.business.demo.article .model.Article.TABLE_NAME;

/**
 * 文章
 */
@ApiModel("文章")
@Data
@TableName(TABLE_NAME)
public class Article extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"article";

    /** 名称 */
    @Excel(name = "名称")
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /** 内容 */
    @Excel(name = "内容")
    @NotBlank(message = "内容不能为空")
    @ApiModelProperty(value = "内容", required = true)
    private String content;

    /** 状态 */
    @Excel(name = "状态")
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    /** 备注 */
    @Excel(name = "备注")
    @ApiModelProperty("备注")
    private String remark;

}
