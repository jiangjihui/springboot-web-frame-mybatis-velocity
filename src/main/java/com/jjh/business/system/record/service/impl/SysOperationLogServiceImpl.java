package com.jjh.business.system.record.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.jjh.business.system.dept.mapper.SysDeptMapper;
import com.jjh.business.system.dept.model.SysDept;
import com.jjh.business.system.record.controller.form.QueryOperationLogForm;
import com.jjh.business.system.record.mapper.SysOperationLogMapper;
import com.jjh.business.system.record.model.SysOperationLog;
import com.jjh.business.system.record.service.SysOperationLogService;
import com.jjh.business.system.user.mapper.SysUserMapper;
import com.jjh.business.system.user.model.SysUser;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 服务层实现
 *
 * @author jjh
 * @date 2020/04/26
 */
@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Resource
    private SysOperationLogMapper sysOperationLogMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysDeptMapper sysDeptMapper;


    /**
     * 查询操作日志列表
     *
     * @param form 查询条件
     * @return 操作日志集合
     */
    @Override
    public List<SysOperationLog> list(PageRequestForm<QueryOperationLogForm> form) {
        List<SysOperationLog> list = sysOperationLogMapper.selectList(form.pageWrapper(SysOperationLog.class));
        if (CollectionUtil.isNotEmpty(list)) {
            for (SysOperationLog operationLog : list) {
                // 用户名称回显
                String operUserId = operationLog.getOperUserId();
                if (StrUtil.isNotBlank(operUserId)) {
                    SysUser sysUser = sysUserMapper.selectById(operUserId);
                    if (sysUser != null) {
                        operationLog.setOperUserName(sysUser.getUsername());
                    }
                }
                // 部门名称回显
                String deptId = operationLog.getDeptId();
                if (StrUtil.isNotBlank(deptId)) {
                    SysDept sysDept = sysDeptMapper.selectById(deptId);
                    if (sysDept != null) {
                        operationLog.setDeptName(sysDept.getName());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 新增操作日志对象
     *
     * @param entity 待新增对象
     * @return 操作日志对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysOperationLog add(SysOperationLog entity) {
        entity.setId(IdGenerateHelper.nextId());
        sysOperationLogMapper.insert(entity);
        return entity;
    }

    /**
     * 更新操作日志对象
     *
     * @param entity 待更新对象
     * @return 操作日志对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysOperationLog update(SysOperationLog entity) {
        SysOperationLog oldEntity = sysOperationLogMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysOperationLogMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 删除操作日志对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        sysOperationLogMapper.deleteBatchIds(CollectionUtil.toList(ids.split(",")));
        return true;
    }

}
