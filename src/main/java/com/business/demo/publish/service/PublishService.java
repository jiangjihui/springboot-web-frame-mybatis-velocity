package com.business.demo.publish.service;

import com.business.demo.publish.dao.PublishMapper;
import com.business.demo.publish.domain.Publish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 通知公告 服务层实现
 * 
 * @author jjh
 * @date Wed Jun 05 09:53:54 CST 2019
 */
@Service
public class PublishService implements IPublishService
{
	@Autowired
	private PublishMapper publishMapper;

	/**
     * 查询通知公告信息
     * 
     * @param id 通知公告ID
     * @return 通知公告信息
     */
    @Override
	public Publish selectPublishById(String id)
	{
	    return publishMapper.selectPublishById(id);
	}
	
	/**
     * 查询通知公告列表
     * 
     * @param params 通知公告信息
     * @return 通知公告集合
     */
	@Override
	public List<Publish> selectPublishList(Map params)
	{
	    return publishMapper.selectPublishList(params);
	}
	
    /**
     * 新增通知公告
     * 
     * @param publish 通知公告信息
     * @return 结果
     */
	@Override
	public int insertPublish(Publish publish)
	{
	    return publishMapper.insertPublish(publish);
	}
	
	/**
     * 修改通知公告
     * 
     * @param publish 通知公告信息
     * @return 结果
     */
	@Override
	public int updatePublish(Publish publish)
	{
	    return publishMapper.updatePublish(publish);
	}

	/**
     * 删除通知公告对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deletePublishByIds(String ids)
	{
		return publishMapper.deletePublishByIds(ids.split(","));
	}
	
}
