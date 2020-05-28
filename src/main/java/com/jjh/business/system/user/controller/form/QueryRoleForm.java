package com.jjh.business.system.user.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 查询角色表单
 *
 * @author jjh
 * @date 2020/04/13
 **/
@ApiModel("查询角色表单")
@Data
public class QueryRoleForm {

    @ApiModelProperty("名称")
    private String name_WithLike;

    @ApiModelProperty("创建日期（起始）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime_WithGreatEqual;

    @ApiModelProperty("创建日期（结束）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime_WithLessEqual;

}
