package com.jjh.common.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务基础实体
 *
 * @EntityListeners 开启jpa审计 https://www.cnblogs.com/niceyoo/p/10908647.html
 */
@ApiModel("业务基础实体")
@Data
public abstract class BaseEntity implements Serializable {

    /** 唯一编号（ID）*/
    @ApiModelProperty("[base]唯一编号（ID）")
    private String id;

    /** 创建日期（yyyy-MM-dd HH:mm:ss）*/
    @ApiModelProperty("[base]创建日期（yyyy-MM-dd HH:mm:ss）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新日期（yyyy-MM-dd HH:mm:ss）*/
    @ApiModelProperty("[base]更新日期（yyyy-MM-dd HH:mm:ss）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;


}
