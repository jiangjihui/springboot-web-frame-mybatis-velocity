package com.jjh.business.system.tenant.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static com.jjh.business.system.tenant.model.SysTenantApplication.TABLE_NAME;

/**
 * 租户子系统关联
 *
 * @author jjh
 * @date 2020/02/18
 */
@ApiModel("租户子系统关联")
@Data
@TableName(TABLE_NAME)
public class SysTenantApplication extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_tenant_application";

    /** 租户编码 */
    @NotBlank(message = "租户编码")
    @ApiModelProperty(value = "租户编码", required = true)
    private String tenantCode;

    /** 子系统编码 */
    @NotBlank(message = "子系统编码")
    @ApiModelProperty(value = "子系统编码", required = true)
    private String appCode;

}
