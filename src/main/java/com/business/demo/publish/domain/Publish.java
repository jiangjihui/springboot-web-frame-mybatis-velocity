package com.business.demo.publish.model;

import lombok.Data;
import com.common.model.AuditBaseEntity;

/**
 * 通知公告表 sys_publish
 * 
 * @author jjh
 * @date Wed Jun 05 09:53:54 CST 2019
 */
@Data
public class Publish extends AuditBaseEntity
{
	private static final long serialVersionUID = 1L;
	
	/** 公告标题 */
	private String noticeTitle;
	/** 公告类型（1通知 2公告） */
	private String noticeType;
	/** 公告内容 */
	private String noticeContent;
	/** 公告状态（0正常 1关闭） */
	private String status;
	/** 备注 */
	private String remark;


}
