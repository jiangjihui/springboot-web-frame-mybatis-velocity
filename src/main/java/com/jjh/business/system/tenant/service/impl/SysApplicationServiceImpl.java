package com.jjh.business.system.tenant.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.tenant.mapper.SysApplicationMapper;
import com.jjh.business.system.tenant.mapper.SysTenantApplicationMapper;
import com.jjh.business.system.tenant.model.SysApplication;
import com.jjh.business.system.tenant.model.SysTenantApplication;
import com.jjh.business.system.tenant.service.SysApplicationService;
import com.jjh.business.system.user.mapper.SysPermissionMapper;
import com.jjh.business.system.user.model.SysPermission;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 子系统 服务层实现
 *
 * @author jjh
 * @date 2020/02/18
 */
@Service
public class SysApplicationServiceImpl implements SysApplicationService {

    @Resource
    private SysApplicationMapper sysApplicationMapper;
    @Resource
    private SysTenantApplicationMapper sysTenantApplicationMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;


    /**
     * 查询子系统列表
     *
     * @param form 查询条件
     * @return 子系统集合
     */
    @Override
    public List<SysApplication> list(PageRequestForm<SysApplication> form) {
        return sysApplicationMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增子系统对象
     *
     * @param entity 待新增对象
     * @return 子系统对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysApplication add(SysApplication entity) {
        if (sysApplicationMapper.selectOne(Wrappers.<SysApplication>lambdaQuery().eq(SysApplication::getAppCode, entity.getAppCode())) != null) {
            throw new BusinessException("该编码已存在，请重新输入");
        }
        entity.setId(IdGenerateHelper.nextId());
        sysApplicationMapper.insert(entity);
        return entity;
    }

    /**
     * 更新子系统对象
     *
     * @param entity 待更新对象
     * @return 子系统对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysApplication update(SysApplication entity) {
        SysApplication oldEntity = sysApplicationMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysApplicationMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除子系统对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        if (StrUtil.isBlank(ids)) {
            return false;
        }
        for (String id : ids.split(",")) {
            SysApplication application = sysApplicationMapper.selectById(id);
            if (application == null) {
                throw new BusinessException("对象不存在，请检查");
            }
            if (CollectionUtil.isNotEmpty(sysTenantApplicationMapper.selectList(Wrappers.<SysTenantApplication>lambdaQuery().eq(SysTenantApplication::getAppCode, application.getAppCode())))) {
                throw new BusinessException("该子系统已关联租户，不可删除");
            }
            if (CollectionUtil.isNotEmpty(sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery().eq(SysPermission::getAppCode, application.getAppCode())))) {
                throw new BusinessException("该子系统已关联权限，不可删除");
            }
            sysApplicationMapper.deleteById(application.getId());
        }
        return true;
    }

    /**
     * 导入数据
     *
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void importData(List<SysApplication> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysApplication entity : list) {
            SysApplication oldEntity = sysApplicationMapper.selectById(entity.getId());
            if (oldEntity != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
                    sysApplicationMapper.updateById(oldEntity);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。");
                }
            }
            else {
                entity.setId(IdGenerateHelper.nextId());
                sysApplicationMapper.insert(entity);
            }
        }
    }

    /**
     * 查询所有子系统
     * @return
     */
    @Override
    public List<SysApplication> queryAll() {
        return sysApplicationMapper.selectList(Wrappers.<SysApplication>lambdaQuery().eq(SysApplication::getStatus, BaseConstants.STATUS_NOMAL));
    }
}
