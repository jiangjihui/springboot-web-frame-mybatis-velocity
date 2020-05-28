package com.jjh.business.system.tenant.controller.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 租户分配子系统 表单
 *
 * @author jjh
 * @date 2020/2/18
 **/
@Data
@ApiModel("租户分配子系统表单")
public class DistributionApplicationForm {

    /** 租户编码*/
    @NotBlank(message = "租户编码不能为空")
    @ApiModelProperty("租户编码")
    private String tenantCode;

    /** 子系统编码*/
    @ApiModelProperty("子系统编码")
    private List<String> appCodeList;

}
