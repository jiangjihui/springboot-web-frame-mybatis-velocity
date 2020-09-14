package com.jjh.business.system.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.user.controller.form.QueryRoleForm;
import com.jjh.business.system.user.controller.form.UpdateUserRoleForm;
import com.jjh.business.system.user.mapper.RolePermissionMappingMapper;
import com.jjh.business.system.user.mapper.SysRoleMapper;
import com.jjh.business.system.user.mapper.SysUserRoleMappingMapper;
import com.jjh.business.system.user.model.RolePermissionMapping;
import com.jjh.business.system.user.model.SysRole;
import com.jjh.business.system.user.model.SysUserRoleMapping;
import com.jjh.business.system.user.service.SysRoleService;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 角色实体类 服务层实现
 *
 * @author jjh
 * @date 2019/11/20
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private RolePermissionMappingMapper rolePermissionMappingMapper;
    @Resource
    private SysUserRoleMappingMapper sysUserRoleMappingMapper;


    /**
     * 查询角色实体类列表
     *
     * @param form 查询条件
     * @return 角色实体类集合
     */
    @Override
    public List<SysRole> list(PageRequestForm<QueryRoleForm> form) {
        return sysRoleMapper.selectList(form.pageWrapper(SysRole.class));
    }

    /**
     * 新增角色实体类对象
     *
     * @param entity 待新增对象
     * @return 角色实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole add(SysRole entity) {
        entity.setId(IdGenerateHelper.nextId());
        // 初始化状态
        if (entity.getStatus() == null) {
            entity.setStatus(BaseConstants.STATUS_NOMAL);
        }
        SysRole sysRole = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getCode, entity.getCode()));;
        if (sysRole != null) {
            throw new BusinessException("该角色代码已存在："+ sysRole.getName());
        }
        sysRoleMapper.insert(entity);
        return entity;
    }

    /**
     * 更新角色实体类对象
     *
     * @param entity 待更新对象
     * @return 角色实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole update(SysRole entity) {
        SysRole oldEntity = sysRoleMapper.selectById(entity.getId());
        if (Objects.isNull(oldEntity)) {
            throw new BusinessException("对象不存在，请检查");
        }
        SysRole sysRole = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getCode, entity.getCode()));;
        if (sysRole != null && !sysRole.getId().equals(oldEntity.getId())) {
            throw new BusinessException("该角色代码已存在："+ sysRole.getName());
        }
        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysRoleMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 删除角色实体类对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        String[] idArray = ids.split(",");
        for (String roleId : idArray) {
            // 删除用户关联
            HashMap<String, Object> params = new HashMap<>();
            sysUserRoleMappingMapper.delete(Wrappers.<SysUserRoleMapping>lambdaQuery().eq(SysUserRoleMapping::getRoleId, roleId));
            //删除权限关联
            rolePermissionMappingMapper.delete(Wrappers.<RolePermissionMapping>lambdaQuery().eq(RolePermissionMapping::getRoleId, roleId));
        }

        sysRoleMapper.deleteBatchIds(CollectionUtil.toList(idArray));
        return true;
    }

    /**
     * 更新用户角色关联
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(UpdateUserRoleForm form) {
        String userId = form.getUserId();
        sysUserRoleMappingMapper.delete(Wrappers.<SysUserRoleMapping>lambdaQuery().eq(SysUserRoleMapping::getUserId, userId));
        if (StrUtil.isBlank(form.getRoleIds())) {
            return;
        }
        // 建立新关联
        String[] roleIdArray = form.getRoleIds().split(",");
        for (String roleId : roleIdArray) {
            SysUserRoleMapping mapping = new SysUserRoleMapping();
            mapping.setId(IdGenerateHelper.nextId());
            mapping.setUserId(userId);
            mapping.setRoleId(roleId);
            sysUserRoleMappingMapper.insert(mapping);
        }
    }

    /**
     * 查询用户角色关联
     * @param userId    用户ID
     * @return  角色ID数组
     */
    @Override
    public List<String> queryUserRole(String userId) {
        List<SysUserRoleMapping> mappings = sysUserRoleMappingMapper.selectList(Wrappers.<SysUserRoleMapping>lambdaQuery().eq(SysUserRoleMapping::getUserId, userId));;
        if (CollectionUtil.isEmpty(mappings)) {
            return null;
        }
        List<String> roleIdList = new ArrayList<String>(mappings.size());
        for (SysUserRoleMapping mapping : mappings) {
            roleIdList.add(mapping.getRoleId());
        }
        return roleIdList;
    }

    /**
     * 查询所有角色
     * @return  角色列表
     */
    @Override
    public List<SysRole> queryAll() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getStatus, BaseConstants.STATUS_NOMAL);
        return sysRoleMapper.selectList(queryWrapper);
    }

}
