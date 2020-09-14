package com.jjh.business.system.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.user.controller.form.QueryUserForm;
import com.jjh.business.system.user.controller.form.ResetCurrentUserPwdForm;
import com.jjh.business.system.user.controller.form.ResetPasswordForm;
import com.jjh.business.system.user.controller.form.UserFrozenForm;
import com.jjh.business.system.user.mapper.SysPermissionMapper;
import com.jjh.business.system.user.mapper.SysRoleMapper;
import com.jjh.business.system.user.mapper.SysUserMapper;
import com.jjh.business.system.user.mapper.SysUserRoleMappingMapper;
import com.jjh.business.system.user.model.SysRole;
import com.jjh.business.system.user.model.SysUser;
import com.jjh.business.system.user.model.SysUserRoleMapping;
import com.jjh.business.system.user.service.SysUserService;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.key.CacheConstants;
import com.jjh.common.util.EncryptUtils;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.framework.jwt.JwtUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 用户信息 服务实现
 *
 * @author jjh
 * @date 2019/9/20
 **/
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserRoleMappingMapper sysUserRoleMappingMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysPermissionMapper sysPermissionMapper;


    @Override
    public List<SysUser> list(PageRequestForm<QueryUserForm> form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        QueryUserForm filter = form.getFilter();
        if (filter != null) {
            queryWrapper.like(StrUtil.isNotBlank(filter.getUsername_WithLike()),"username", filter.getUsername_WithLike());
            Map<Object, Object> param = MapUtil.builder()
                    .put("sex", filter.getSex())
                    .put("phone", filter.getPhone())
                    .put("status", filter.getStatus())
                    .put("email", filter.getEmail())
                    .build();
            queryWrapper.allEq(param, false);
            queryWrapper.ge(filter.getCreateTime_WithGreatEqual() != null, "create_time", filter.getCreateTime_WithGreatEqual());
            queryWrapper.le(filter.getCreateTime_WithLessEqual() != null, "create_time", filter.getCreateTime_WithLessEqual());
        }
        form.pageWrapper();
        List<SysUser> list = sysUserMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            for (SysUser user : list) {
                // 回显角色
                user.setRoleCode(sysRoleMapper.listSysRoleCode(user.getId()));
                user.setRoleList(sysRoleMapper.listSysRole(user.getId()));
            }
        }
        return list;
    }

    /**
     * 根据用户名查找用户
     * @param username  用户名
     * @return
     */
    @Override
    @Cacheable(cacheNames= CacheConstants.SYS_USER_NAME, key="#username", unless = "#result == null")
    public SysUser findByUsername(String username) {
        return sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
    }

    /**
     * 根据用户ID查找用户
     * @param id  用户ID
     * @return
     */
    @Override
    @Cacheable(cacheNames= CacheConstants.SYS_USER_ID, key="#id", unless = "#result == null")
    public SysUser findById(String id) {
		return sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getId, id));
    }

    /**
     * 创建用户
     * @param entity  用户信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUser add(SysUser entity) {
        entity.setId(IdGenerateHelper.nextId());
        String username = entity.getUsername();
        String password = entity.getPassword();

        // 用户名查重
        SysUser u = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, username));
        if (u != null) {
            throw new BusinessException("用户名已存在，请检查~");
        }

        String salt = String.valueOf(System.currentTimeMillis());
        entity.setSalt(salt);
        // 密码加密（与shiro密码校验时的解密机制一致）
        String passwd = EncryptUtils.encryptPassword(username, password, entity.getSalt());
        entity.setPassword(passwd);
        // 初始化状态
        if (entity.getStatus() == null) {
            entity.setStatus(BaseConstants.STATUS_NOMAL);
        }
        // 添加新角色关联
        if (CollectionUtil.isNotEmpty(entity.getRoleCode())) {
            for (String roleCode : entity.getRoleCode()) {
                SysRole role = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getCode, roleCode));
                if (role == null) {
                    throw new BusinessException("未找到该角色信息，请检查");
                }
                SysUserRoleMapping userRoleMapping = new SysUserRoleMapping();
                userRoleMapping.setId(IdGenerateHelper.nextId());
                userRoleMapping.setUserId(entity.getId());
                userRoleMapping.setRoleId(role.getId());
                sysUserRoleMappingMapper.insert(userRoleMapping);
            }
        }
        sysUserMapper.insert(entity);
        return entity;
    }

    /**
     * 更新
     *
     * @param entity 用户信息
     * @return 用户信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_USER_NAME, CacheConstants.SYS_USER_ID}, allEntries = true)
    public SysUser update(SysUser entity) {
        SysUser oldEntity = sysUserMapper.selectById(entity.getId());
        if (oldEntity == null) {
            throw new BusinessException("未找到该用户，请检查");
        }
        /* 更新角色信息 */
        // 清理旧的角色关联
        sysUserRoleMappingMapper.delete(Wrappers.<SysUserRoleMapping>lambdaQuery().eq(SysUserRoleMapping::getUserId, oldEntity.getId()));
        // 添加新角色关联
        if (CollectionUtil.isNotEmpty(entity.getRoleCode())) {
            for (String roleCode : entity.getRoleCode()) {
                SysRole role = sysRoleMapper.selectOne(Wrappers.<SysRole>lambdaQuery().eq(SysRole::getCode, roleCode));
                if (role == null) {
                    throw new BusinessException("未找到该角色信息，请检查");
                }
                SysUserRoleMapping userRoleMapping = new SysUserRoleMapping();
                userRoleMapping.setId(IdGenerateHelper.nextId());
                userRoleMapping.setUserId(entity.getId());
                userRoleMapping.setRoleId(role.getId());
                sysUserRoleMappingMapper.insert(userRoleMapping);
            }
        }
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysUserMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 删除用户
     * @param ids 待删除的ID数组
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_USER_NAME, CacheConstants.SYS_USER_ID}, allEntries = true)
    public void delete(@NotNull String ids) {
        // 删除角色关联
        String[] idArray = ids.split(",");
        for (String userId : idArray) {
            sysUserRoleMappingMapper.delete(Wrappers.<SysUserRoleMapping>lambdaQuery().eq(SysUserRoleMapping::getUserId, userId));
        }
        sysUserMapper.deleteBatchIds(CollectionUtil.toList(idArray));
    }

    /**
     *  更新用户密码
     * @param form 密码表单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_USER_NAME}, allEntries = true)
    public SysUser resetPassword(ResetPasswordForm form) {
        SysUser sysUser = sysUserMapper.selectById(form.getId());
        if (sysUser == null) {
            throw new BusinessException("未找到该用户，请检查");
        }
        String passwd = EncryptUtils.encryptPassword(sysUser.getUsername(), form.getNewPassword(), sysUser.getSalt());
        sysUser.setPassword(passwd);
        sysUserMapper.updateById(sysUser);
        return sysUser;
    }

    /**
     *  获取角色Code
     * @param userId    用户ID
     */
    @Override
    public List<String> listSysRoleCode(String userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException("未找到该用户，请检查");
        }
        if (BaseConstants.SYS_ADMIN.equals(sysUser.getUsername())) {
            return CollectionUtil.toList(new String[]{"admin"});
        }
        return sysRoleMapper.listSysRoleCodeByStatus(userId, BaseConstants.STATUS_NOMAL);
    }

    /**
     *  获取权限Code
     * @param userId    用户ID
     */
    @Override
    public List<String> listSysPermissionCode(String userId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException("未找到该用户，请检查");
        }
        if (BaseConstants.SYS_ADMIN.equals(sysUser.getUsername())) {
            return sysPermissionMapper.listAllSysPermissionCode();
        }
        return sysPermissionMapper.listSysPermissionCode(userId);
    }

    /**
     * 冻结/解冻
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_USER_NAME}, allEntries = true)
    public void frozen(List<UserFrozenForm> list) {
        for (UserFrozenForm form : list) {
            SysUser sysUser = sysUserMapper.selectById(form.getId());
            if (sysUser == null) {
                throw new BusinessException("未找到该用户，请检查");
            }
            sysUser.setStatus(form.getStatus());
            sysUserMapper.updateById(sysUser);
        }
    }

    /**
     * 导入数据
     * @param list
     * @param updateSupport 是否更新现有数据
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {CacheConstants.SYS_USER_NAME}, allEntries = true)
    public void importData(List<SysUser> list, Boolean updateSupport) {
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("待导入数据为空");
        }
        for (SysUser sysUser : list) {
            if (StrUtil.isBlank(sysUser.getUsername())) {
                throw new BusinessException("导入失败，用户名为空。请检查");
            }
            SysUser oldSysUser = this.findByUsername(sysUser.getUsername());
            if (oldSysUser != null) {
                if (Boolean.TRUE.equals(updateSupport)) {
                    PojoUtils.copyPropertiesIgnoreNull(sysUser, oldSysUser);
                    sysUserMapper.updateById(oldSysUser);
//                    sysUserRepository.save(oldSysUser);
                }
            }
            else {
                sysUser.setId(IdGenerateHelper.nextId());
                // 密码重置
                sysUser.setSalt(String.valueOf(System.currentTimeMillis()));
                String password = EncryptUtils.encryptPassword(sysUser.getUsername(), sysUser.getPassword(), sysUser.getSalt());
                sysUser.setPassword(password);
                if (sysUser.getStatus() == null) {
                    sysUser.setStatus(BaseConstants.STATUS_NOMAL);
                }
                sysUserMapper.insert(sysUser);
//                sysUserRepository.save(oldSysUser);
            }
        }
    }

    /**
     * 获取用户信息
     * @param id    用户ID
     * @return
     */
    @Override
    public SysUser info(String id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            throw new BusinessException("未找到该用户，请检查");
        }
        //获取用户角色
        List<String> roleList = this.listSysRoleCode(sysUser.getId());
        // 获取用户权限
        List<String> permissionList = this.listSysPermissionCode(sysUser.getId());
        sysUser.setRoleCode(roleList);
        sysUser.setPermissionCode(permissionList);
        return sysUser;
    }

    /**
     * 重置当前用户密码
     * @param form 密码表单
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysUser resetCurrentUserPwd(ResetCurrentUserPwdForm form) {
        String userId = JwtUtil.getUserId();
        //获取当前用户
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException("未找到该用户，请检查");
        }
        //验证旧密码是否正确
       String oldPassword = sysUser.getPassword();
       String encryptPassword = EncryptUtils.encryptPassword(sysUser.getUsername(), form.getOldPassword(), sysUser.getSalt());
       if(!oldPassword.equals(encryptPassword)) {
           throw   new BusinessException("旧密码输入错误！");
       }
       //密码重置
        String newEncryptPassword= EncryptUtils.encryptPassword(sysUser.getUsername(), form.getNewPassword(), sysUser.getSalt());
        sysUser.setPassword(newEncryptPassword);
        sysUserMapper.updateById(sysUser);
        return sysUser;
    }

    /**
     * 根据用户名称获取用户信息
     * @param name  用户名称
     * @return
     */
    @Override
    public List<SysUser> findByName(String name) {
        return sysUserMapper.selectList(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getName, name));
    }

}
