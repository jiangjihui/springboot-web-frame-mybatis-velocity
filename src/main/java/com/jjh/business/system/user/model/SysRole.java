package com.jjh.business.system.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.jjh.business.system.user.model.SysRole.TABLE_NAME;

/**
 * 角色实体类
 *
 * @author jjh
 * @date 2019/11/19
 */
@ApiModel("角色实体类")
@Data
@TableName(TABLE_NAME)
public class SysRole extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_role";

    /** 角色名称 */
    @NotBlank(message = "角色名称不能为空")
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

    /** 角色代码 */
    @NotBlank(message = "角色代码不能为空")
    @ApiModelProperty(value = "角色代码", required = true)
    private String code;

    /** 描述 */
    @ApiModelProperty("描述")
    private String description;

    /** 状态 */
    @ApiModelProperty(value = "状态", required = true)
    private Integer status;

    /** 数据范围 （1：所有数据权限；2：本部门及以下数据权限）*/
    @ApiModelProperty(value = "数据范围", notes = "1：所有数据权限；2：本部门及以下数据权限")
    private Integer dataScope;

    /** 租户编码 */
    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    /** 权限列表 */
    @ApiModelProperty("权限列表")
    @TableField(exist = false)
    private List<SysPermission> permissionList;

}
