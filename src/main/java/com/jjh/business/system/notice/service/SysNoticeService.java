package com.jjh.business.system.notice.service;

import com.jjh.business.system.notice.controller.form.QuerySysNoticeForm;
import com.jjh.business.system.notice.model.SysNotice;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知 服务层
 *
 * @author jjh
 * @date 2020/05/07
 */
public interface SysNoticeService {

    /**
     * 查询通知列表
     *
     * @param form 查询条件
     * @return 通知集合
     */
    List<SysNotice> list(PageRequestForm<QuerySysNoticeForm> form);

    /**
     * 新增通知对象
     *
     * @param entity 待新增对象
     * @return 通知对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysNotice add(SysNotice entity);


    /**
     * 更新通知对象
     *
     * @param entity 待更新对象
     * @return 通知对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysNotice update(SysNotice entity);


    /**
     * 删除通知对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);

    /**
     * 个人通知
     * @param form
     * @return
     */
    List<SysNotice> listSelf(PageRequestForm<SysNotice> form);

    /**
     * 更新为已读
     * @param id    通知ID
     */
    void updateReadFlag(String id);
}
