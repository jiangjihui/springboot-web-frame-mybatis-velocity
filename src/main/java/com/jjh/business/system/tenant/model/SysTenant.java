package com.jjh.business.system.tenant.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import com.jjh.framework.plugin.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static com.jjh.business.system.tenant.model.SysTenant.TABLE_NAME;

/**
 * 租户信息
 *
 * @author jjh
 * @date 2020/02/18
 */
@ApiModel("租户信息")
@Data
@TableName(TABLE_NAME)
public class SysTenant extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_tenant";

    /** 名称 */
    @Excel(name = "名称")
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /** 租户编码 */
    @Excel(name = "租户编码")
    @NotBlank(message = "租户编码不能为空")
    @ApiModelProperty(value = "租户编码", required = true)
    private String tenantCode;

    /** 联系人 */
    @Excel(name = "联系人")
    @ApiModelProperty("联系人")
    private String contactMan;

    /** 手机号码 */
    @Excel(name = "手机号码")
    @ApiModelProperty("手机号码")
    private String contactNumber;

    /** 地址 */
    @Excel(name = "地址")
    @ApiModelProperty("地址")
    private String address;

    /** 描述 */
    @Excel(name = "描述")
    @ApiModelProperty("描述")
    private String description;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty(value = "状态", notes = "0=正常,1=停用", required = true)
    private Integer status;


}
