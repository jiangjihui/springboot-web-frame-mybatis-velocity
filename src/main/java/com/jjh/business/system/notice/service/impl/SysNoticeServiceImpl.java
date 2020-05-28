package com.jjh.business.system.notice.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.jjh.business.system.notice.controller.form.QuerySysNoticeForm;
import com.jjh.business.system.notice.mapper.SysNoticeMapper;
import com.jjh.business.system.notice.model.SysNotice;
import com.jjh.business.system.notice.service.SysNoticeService;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.framework.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知 服务层实现
 *
 * @author jjh
 * @date 2020/05/07
 */
@Service
public class SysNoticeServiceImpl implements SysNoticeService {

    @Resource
    private SysNoticeMapper sysNoticeMapper;


    /**
     * 查询通知列表
     *
     * @param form 查询条件
     * @return 通知集合
     */
    @Override
    public List<SysNotice> list(PageRequestForm<QuerySysNoticeForm> form) {
        return sysNoticeMapper.selectList(form.pageWrapper(SysNotice.class));
    }

    /**
     * 新增通知对象
     *
     * @param entity 待新增对象
     * @return 通知对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysNotice add(SysNotice entity) {
        entity.setId(IdGenerateHelper.nextId());
        sysNoticeMapper.insert(entity);
        return entity;
    }

    /**
     * 更新通知对象
     *
     * @param entity 待更新对象
     * @return 通知对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysNotice update(SysNotice entity) {
        SysNotice oldEntity = sysNoticeMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysNoticeMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 删除通知对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        sysNoticeMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

    /**
     * 个人通知
     * @param form
     * @return
     */
    @Override
    public List<SysNotice> listSelf(PageRequestForm<SysNotice> form) {
        SysNotice filter = form.getFilter();
        if (filter == null) {
            filter = new SysNotice();
            form.setFilter(filter);
        }
        filter.setReceiver(JwtUtil.getUserId());
        return sysNoticeMapper.selectList(form.queryWrapper());
    }

    /**
     * 更新为已读
     * @param id    通知ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateReadFlag(String id) {
        SysNotice sysNotice = sysNoticeMapper.selectById(id);
        if (sysNotice == null) {
            throw new BusinessException("未找到待更新的通知记录");
        }
        sysNotice.setReadFlag(Boolean.TRUE);
        sysNoticeMapper.updateById(sysNotice);
    }
}
