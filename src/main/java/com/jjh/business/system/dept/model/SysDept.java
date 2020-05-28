package com.jjh.business.system.dept.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import com.jjh.framework.plugin.excel.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.jjh.business.system.dept.model.SysDept.TABLE_NAME;

/**
 * 部门
 *
 * @author jjh
 * @date 2020/04/21
 */
@ApiModel("部门")
@Data
@TableName(TABLE_NAME)
public class SysDept extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_dept";

    /** 名称 */
    @Excel(name = "名称")
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /** 部门编码 */
    @Excel(name = "部门编码")
    @NotBlank(message = "部门编码不能为空")
    @ApiModelProperty(value = "部门编码", required = true)
    private String deptCode;

    /** 父部门ID */
    @Excel(name = "父部门ID")
    @ApiModelProperty(value = "父部门ID")
    private String parentId;

    /** 菜单排序 */
    @Excel(name = "菜单排序")
    @ApiModelProperty("菜单排序")
    private Integer sortNo;

    /** 联系电话 */
    @Excel(name = "联系电话")
    @ApiModelProperty("联系电话")
    private String phone;

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


    /** children 子部门 */
    @ApiModelProperty("children 子部门")
    @TableField(exist = false)
    private List<SysDept> children;
}
