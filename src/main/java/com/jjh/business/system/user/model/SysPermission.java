package com.jjh.business.system.user.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.jjh.business.system.user.model.SysPermission.TABLE_NAME;

/**
 * 权限实体类
 */
@ApiModel("权限实体类")
@Data
@TableName(TABLE_NAME)
public class SysPermission extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_permission";

    /** 父编号 */
    @ApiModelProperty("父编号")
    private String parentId;

    /** 名称 */
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /** 权限代码（menu例子：role:*，button例子：role:create,role:update,role:delete,role:view） */
    @NotBlank(message = "权限代码不能为空")
    @ApiModelProperty(value = "权限代码（menu例子：role:*，button例子：role:create,role:update,role:delete,role:view）", required = true)
    private String code;

    /** 类型（0：一级菜单；1：子菜单 ；2：按钮权限） */
    @NotBlank(message = "类型不能为空")
    @ApiModelProperty(value = "类型（0：一级菜单；1：子菜单 ；2：按钮权限）", required = true)
    private String menuType;

    /** 菜单图标 */
    @ApiModelProperty("菜单图标")
    private String icon;

    /** 组件 */
    @ApiModelProperty("组件")
    private String component;

    /** 路径 */
    @ApiModelProperty("路径")
    private String url;

    /** 菜单排序 */
    @ApiModelProperty("菜单排序")
    private Integer sortNo;

    /** 描述 */
    @ApiModelProperty("描述")
    private String description;

    /** 状态（0=正常,1=停用） */
    @ApiModelProperty("状态（0=正常,1=停用）")
    private Integer status;

    /** 子系统编码 */
    @ApiModelProperty(value = "子系统编码")
    private String appCode;

    /** 是否隐藏路由菜单: 0否,1是（默认值0） */
    @ApiModelProperty(value = "是否隐藏路由菜单")
    private Boolean hidden;

    /** 外链菜单打开方式: 0/内部打开 1/外部打开 */
    @ApiModelProperty(value = "打开方式（true-内部/false-外部）")
    private Boolean internalOrExternal;

    /** children 子权限 */
    @ApiModelProperty("children 子权限")
    @TableField(exist = false)
    private List<SysPermission> children;

}
