package com.jjh.business.system.tenant.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.tenant.controller.form.DistributionApplicationForm;
import com.jjh.business.system.tenant.mapper.SysTenantApplicationMapper;
import com.jjh.business.system.tenant.mapper.SysTenantMapper;
import com.jjh.business.system.tenant.model.SysTenant;
import com.jjh.business.system.tenant.model.SysTenantApplication;
import com.jjh.business.system.tenant.service.SysTenantService;
import com.jjh.business.system.user.mapper.*;
import com.jjh.business.system.user.model.*;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.EncryptUtils;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户信息 服务层实现
 *
 * @author jjh
 * @date 2020/02/18
 */
@Service
public class SysTenantServiceImpl implements SysTenantService {

    @Resource
    private SysTenantMapper sysTenantMapper;
    @Resource
    private SysTenantApplicationMapper sysTenantApplicationMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMappingMapper sysUserRoleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private RolePermissionMappingMapper sysRolePermissionMapper;


    /**
     * 查询租户信息列表
     *
 * @param form 查询条件
     * @return 租户信息集合
     */
    @Override
    public List<SysTenant> list(PageRequestForm<SysTenant> form) {
        return sysTenantMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增租户信息对象
     *
     * @param entity 待新增对象
     * @return 租户信息对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysTenant add(SysTenant entity) {
        if (sysTenantMapper.selectOne(Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getTenantCode, entity.getTenantCode())) != null) {
            throw new BusinessException("该编码已存在，请重新输入");
        }
        entity.setId(IdGenerateHelper.nextId());
        sysTenantMapper.insert(entity);
        return entity;
    }

    /**
     * 更新租户信息对象
     *
     * @param entity 待更新对象
     * @return 租户信息对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysTenant update(SysTenant entity) {
        SysTenant oldEntity = sysTenantMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("对象不存在，请检查");
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysTenantMapper.updateById(oldEntity);
        return entity;
    }

    /**
     * 删除租户信息对象
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
            SysTenant tenant = sysTenantMapper.selectById(id);
            if (tenant == null) {
                throw new BusinessException("对象不存在，请检查");
            }
            if (CollectionUtil.isNotEmpty(sysTenantApplicationMapper.selectList(Wrappers.<SysTenantApplication>lambdaQuery().eq(SysTenantApplication::getTenantCode, tenant.getTenantCode())))) {
                throw new BusinessException("该租户已关联子系统，不可删除，请先解除子系统关联");
            }
            sysTenantMapper.deleteById(tenant.getId());
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
    public void importData(List<SysTenant> list, boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysTenant entity : list) {
            SysTenant oldEntity = sysTenantMapper.selectById(entity.getId());
            if (oldEntity != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
                    sysTenantMapper.updateById(oldEntity);
                }
                else {
                    throw new BusinessException("存在重复数据，请检查。");
                }
            }
            else {
                entity.setId(IdGenerateHelper.nextId());
                sysTenantMapper.insert(entity);
            }
        }
    }

    /**
     * 租户分配子系统
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void distributionApplication(DistributionApplicationForm form) {
        String tenantCode = form.getTenantCode();
        SysTenant tenant = sysTenantMapper.selectOne(Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getTenantCode, tenantCode));
        if (tenant == null) {
            throw new BusinessException("未找到该租户信息，请检查");
        }

        // 原已分配子系统编码集合
        List<String> oldAppCodeList = new ArrayList<>();
        // 新分配子系统编码集合
        List<String> newAppCodeList = form.getAppCodeList() != null ? form.getAppCodeList() : new ArrayList<>();

        /* 过滤本次修改后，新分配和取消分配的子系统*/
        // 查找原有已分配子系统
        List<SysTenantApplication> tenantApplicationList = sysTenantApplicationMapper.selectList(Wrappers.<SysTenantApplication>lambdaQuery().eq(SysTenantApplication::getTenantCode, tenantCode));
        if (CollectionUtil.isNotEmpty(tenantApplicationList)) {
            oldAppCodeList = tenantApplicationList.stream().map(c -> c.getAppCode()).collect(Collectors.toList());
        }

        // 新增子系统编码集合
        List<String> addAppCodeList = new ArrayList<>();
        // 取消子系统编码集合
        List<String> cancelAppCodeList = new ArrayList<>();

        // 获取本次新增子系统集合
        for (String newAppCode : newAppCodeList) {
            if (!oldAppCodeList.contains(newAppCode)) {
                addAppCodeList.add(newAppCode);
            }
        }
        // 获取本次取消的子系统集合
        for (String oldAppCode : oldAppCodeList) {
            if (!newAppCodeList.contains(oldAppCode)) {
                cancelAppCodeList.add(oldAppCode);
            }
        }

        // 处理本次取消的子系统
        if (CollectionUtil.isNotEmpty(cancelAppCodeList)) {
            for (String cancelAppCode : cancelAppCodeList) {
                // 清理角色/菜单
                List<SysRole> roleList = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getTenantCode, tenantCode));
                List<SysPermission> permissionList = sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery().eq(SysPermission::getAppCode, cancelAppCode));
                for (SysRole role : roleList) {
                    for (SysPermission permission : permissionList) {
                        sysRolePermissionMapper.delete(Wrappers.<RolePermissionMapping>lambdaQuery().eq(RolePermissionMapping::getRoleId, role.getId()).eq(RolePermissionMapping::getPermissionId, permission.getId()));
                    }
                }
                // 清理租户关联
                sysTenantApplicationMapper.delete(Wrappers.<SysTenantApplication>lambdaQuery().eq(SysTenantApplication::getTenantCode, tenantCode).eq(SysTenantApplication::getAppCode, cancelAppCode));
            }
        }

        // 处理本次添加的子系统
        if (CollectionUtil.isNotEmpty(addAppCodeList)) {
            for (String addAppCode : addAppCodeList) {
                // 添加租户关联
                SysTenantApplication tenantApplication = new SysTenantApplication();
                tenantApplication.setId(IdGenerateHelper.nextId());
                tenantApplication.setTenantCode(tenantCode);
                tenantApplication.setAppCode(addAppCode);
                sysTenantApplicationMapper.insert(tenantApplication);

                /* 添加子系统对的菜单与角色关联 */
                List<SysRole> roleList = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getTenantCode, tenantCode));
                List<SysPermission> permissionList = sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery().eq(SysPermission::getAppCode, addAppCode));
                if (CollectionUtil.isEmpty(roleList)) {
                    // 初始化租户下的角色
                    this.initTenantUserAndRole(tenant);
                    roleList = sysRoleMapper.selectList(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getTenantCode, tenantCode));
                }
                // 添加菜单与角色关联
                for (SysRole role : roleList) {
                    for (SysPermission permission : permissionList) {
                        RolePermissionMapping rolePermissionMapping = new RolePermissionMapping();
                        rolePermissionMapping.setId(IdGenerateHelper.nextId());
                        rolePermissionMapping.setRoleId(role.getId());
                        rolePermissionMapping.setPermissionId(permission.getId());
                        sysRolePermissionMapper.insert(rolePermissionMapping);
                    }
                }
            }
        }

    }

    /**
     * 获取租户子系统
     * @param tenantCode 租户编码
     * @return
     */
    @Override
    public List<String> findDistributionApplication(String tenantCode) {
        List<SysTenantApplication> tenantApplicationList = sysTenantApplicationMapper.selectList(Wrappers.<SysTenantApplication>lambdaQuery().eq(SysTenantApplication::getTenantCode, tenantCode));
        if (CollectionUtil.isNotEmpty(tenantApplicationList)) {
            // 获取所有appCode
            return tenantApplicationList.stream().map(SysTenantApplication::getAppCode).collect(Collectors.toList());
        }
        return new ArrayList<String>();
    }

    /**
     * 初始化租户下的角色
     * （所属租户角色，至少有一个）
     * @param tenant
     */
    private void initTenantUserAndRole(SysTenant tenant) {
        // 创建租户默认管理员角色
        SysRole role = new SysRole();
        role.setId(IdGenerateHelper.nextId());
        role.setCode(tenant.getTenantCode());
        role.setName(tenant.getName()+"[admin]");
        role.setStatus(BaseConstants.STATUS_NOMAL);
        role.setTenantCode(tenant.getTenantCode());
        role.setDescription(role.getName());
        sysRoleMapper.insert(role);

        // 创建租户默认管理员用户
        if (sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getTenantCode, tenant.getTenantCode())) == 0) {
            SysUser user = new SysUser();
            user.setId(IdGenerateHelper.nextId());
            user.setName(tenant.getName() + "[admin]");
            user.setUsername(tenant.getTenantCode());
            user.setSalt(String.valueOf(System.currentTimeMillis()));
            user.setPassword(EncryptUtils.encryptPassword(user.getUsername(), "123456", user.getSalt()));
            user.setStatus(BaseConstants.STATUS_NOMAL);
            user.setTenantCode(tenant.getTenantCode());
            sysUserMapper.insert(user);

            // 创建用户角色关联
            SysUserRoleMapping userRoleMapping = new SysUserRoleMapping();
            userRoleMapping.setId(IdGenerateHelper.nextId());
            userRoleMapping.setUserId(user.getId());
            userRoleMapping.setRoleId(role.getId());
            sysUserRoleMapper.insert(userRoleMapping);
        }

        return;
    }
}
