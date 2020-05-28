package com.jjh.business.system.notice.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jjh.common.model.AuditBaseEntity;
import com.jjh.framework.plugin.excel.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

import static com.jjh.business.system.notice.model.SysNotice.TABLE_NAME;

/**
 * 通知
 *
 * @author jjh
 * @date 2020/05/07
 */
@ApiModel("通知")
@Data
@TableName(TABLE_NAME)
public class SysNotice extends AuditBaseEntity {

    public static final String TABLE_NAME = TABLE_PREFIX+"sys_notice";

    /** 标题 */
    @Excel(name = "标题")
    @Size(min = 0, max = 50, message = "公告标题不能超过50个字符")
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题", required = true)
    private String title;

    /** 公告类型（1通知 2公告）  */
    @Excel(name = "公告类型", readConverterExp = "1=通知,2=公告")
    @ApiModelProperty(value = "公告类型 ", notes = "1=通知,2=公告")
    private String noticeType;

    /** 内容 */
    @Excel(name = "内容")
    @ApiModelProperty("内容")
    private String content;

    /** 阅读状态 */
    @Excel(name = "阅读状态", readConverterExp = "false=未读,true=已读")
    @ApiModelProperty(value = "阅读状态", notes = "false=未读,true=已读")
    private Boolean readFlag;

    /** 状态 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    @ApiModelProperty(value = "状态", notes = "0=正常,1=停用", required = true)
    private Integer status;

    /** 来源类型 */
    @Excel(name = "来源类型", readConverterExp = "flow=流程,update=数据变更,expire=到期")
    @ApiModelProperty(value = "来源类型", notes = "flow=流程,update=数据变更,expire=到期")
    private String sourceType;

    /** 发布时间*/
    @Excel(name = "发布时间")
    @ApiModelProperty("发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;

    /** 关联业务ID */
    @ApiModelProperty("关联业务ID")
    private String relativeId;

    /** 发布人 */
    @Excel(name = "发布人")
    @ApiModelProperty("发布人")
    private String sender;

    /** 收件人 */
    @Excel(name = "收件人")
    @ApiModelProperty("收件人")
    private String receiver;

    /** 所属部门ID */
    @ApiModelProperty("所属部门ID")
    private String deptId;

}
