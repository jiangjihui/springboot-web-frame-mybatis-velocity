package com.jjh.business.system.record.service;

import com.jjh.business.system.record.controller.form.QueryOperationLogForm;
import com.jjh.business.system.record.model.SysOperationLog;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author jjh
 * @date 2020/04/26
 */
public interface SysOperationLogService {

    /**
     * 查询操作日志列表
     *
     * @param form 查询条件
     * @return 操作日志集合
     */
    List<SysOperationLog> list(PageRequestForm<QueryOperationLogForm> form);

    /**
     * 新增操作日志对象
     *
     * @param entity 待新增对象
     * @return 操作日志对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysOperationLog add(SysOperationLog entity);


    /**
     * 更新操作日志对象
     *
     * @param entity 待更新对象
     * @return 操作日志对象
     */
    @Transactional(rollbackFor = Exception.class)
    SysOperationLog update(SysOperationLog entity);


    /**
     * 删除操作日志对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    boolean del(String ids);
}
