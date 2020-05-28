package com.jjh.business.system.tenant.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import com.jjh.framework.plugin.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static com.jjh.business.system.tenant.model.SysApplication.TABLE_NAME;

/**
 * 子系统
 *
 * @author jjh
 * @date 2020/02/18
 */
@ApiModel("子系统")
@Data
@TableName(TABLE_NAME)
public class SysApplication extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_application";

    /** 名称 */
    @Excel(name = "名称")
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /** 子系统编码 */
    @Excel(name = "子系统编码")
    @NotBlank(message = "子系统编码不能为空")
    @ApiModelProperty(value = "子系统编码", required = true)
    private String appCode;

    /** 子系统访问地址 */
    @Excel(name = "子系统访问地址")
    @ApiModelProperty("子系统访问地址")
    private String appUri;

    /** 子系统类型 */
    @Excel(name = "子系统类型", readConverterExp = "0=内置,1=租户")
    @ApiModelProperty(value = "子系统类型", notes = "0=内置,1=租户")
    private Integer appType;

    /** 子系统部署路径 */
    @Excel(name = "子系统部署路径")
    @ApiModelProperty("子系统部署路径")
    private String appAddress;

    /** 描述 */
    @Excel(name = "描述")
    @ApiModelProperty("描述")
    private String description;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty(value = "状态", notes = "0=正常,1=停用", required = true)
    private Integer status;


}
