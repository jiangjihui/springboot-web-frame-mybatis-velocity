package com.jjh.business.system.support.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static com.jjh.business.system.support.model.SysProvincial.TABLE_NAME;

/**
 * SysProvincial
 *
 * @author jjh
 * @date 2019/12/13
 **/
@ApiModel("地区省份")
@Data
@TableName(TABLE_NAME)
public class SysProvincial extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_provincial";

    /** 地区省份编号 */
    @ApiModelProperty("地区省份编号")
    private Integer code;

    /** 地区省份名称 */
    @ApiModelProperty("地区省份名称")
    private String name;

    /** 上级地区编号 */
    @ApiModelProperty("上级地区编号")
    private Integer parentCode;

    /** 子地区（回显使用） */
    @ApiModelProperty("子地区（回显使用）")
    @TableField(exist = false)
    private SysProvincial children;
}
