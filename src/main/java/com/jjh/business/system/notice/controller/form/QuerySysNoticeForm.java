package com.jjh.business.system.notice.controller.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 查询通知表单
 *
 * @author jjh
 * @date 2020/05/11
 **/
@ApiModel("查询通知表单")
@Data
public class QuerySysNoticeForm {

    @ApiModelProperty("标题")
    private String title_WithLike;

    @ApiModelProperty(value = "阅读状态", notes = "false=未读,true=已读")
    private Boolean readFlag;

    @ApiModelProperty("发布时间（起始）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime_WithGreatEqual;

    @ApiModelProperty("发布时间（结束）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime_WithLessEqual;

}
