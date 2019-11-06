package com.business.demo.publish.dao;

import com.business.demo.publish.domain.Publish;
import java.util.List;
import java.util.Map;

/**
 * 通知公告 数据层
 * 
 * @author jjh
 * @date Wed Jun 05 09:53:54 CST 2019
 */
public interface PublishMapper 
{
	/**
     * 查询通知公告信息
     * 
     * @param id 通知公告ID
     * @return 通知公告信息
     */
	public Publish selectPublishById(String id);
	
	/**
     * 查询通知公告列表
     * 
     * @param params 通知公告信息
     * @return 通知公告集合
     */
	public List<Publish> selectPublishList(Map params);
	
	/**
     * 新增通知公告
     * 
     * @param publish 通知公告信息
     * @return 结果
     */
	public int insertPublish(Publish publish);
	
	/**
     * 修改通知公告
     * 
     * @param publish 通知公告信息
     * @return 结果
     */
	public int updatePublish(Publish publish);
	
	/**
     * 删除通知公告
     * 
     * @param id 通知公告ID
     * @return 结果
     */
	public int deletePublishById(String id);
	
	/**
     * 批量删除通知公告
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deletePublishByIds(String[] ids);
	
}