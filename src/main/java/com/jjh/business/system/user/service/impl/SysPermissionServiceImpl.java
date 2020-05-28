package com.jjh.business.system.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jjh.business.system.user.controller.form.UpdateRolePermissionForm;
import com.jjh.business.system.user.mapper.RolePermissionMappingMapper;
import com.jjh.business.system.user.mapper.SysPermissionMapper;
import com.jjh.business.system.user.model.RolePermissionMapping;
import com.jjh.business.system.user.model.SysPermission;
import com.jjh.business.system.user.service.SysPermissionService;
import com.jjh.common.constant.BaseConstants;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.common.util.PojoUtils;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.framework.jwt.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 权限实体类 服务层实现
 *
 * @author jjh
 * @date 2019/11/20
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private RolePermissionMappingMapper rolePermissionMappingMapper;

    /**
     * 查询权限实体类列表
     *
     * @param form 查询条件
     * @return 权限实体类集合
     */
    @Override
    public List<SysPermission> list(PageRequestForm<SysPermission> form) {
        return sysPermissionMapper.selectList(form.pageWrapper());
    }

    /**
     * 新增权限实体类对象
     *
     * @param entity 待新增对象
     * @return 权限实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPermission add(SysPermission entity) {
        entity.setId(IdGenerateHelper.nextId());
        // 初始化状态
        if (entity.getStatus() == null) {
            entity.setStatus(BaseConstants.STATUS_NOMAL);
        }
        sysPermissionMapper.insert(entity);
        return entity;
    }

    /**
     * 更新权限实体类对象
     *
     * @param entity 待更新对象
     * @return 权限实体类对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPermission update(SysPermission entity) {
        SysPermission oldEntity = sysPermissionMapper.selectById(entity.getId());
        if (Objects.isNull(oldEntity)) {
            throw new BusinessException("对象不存在，请检查");
        }
        // 更新菜单所属子系统
        if (!StrUtil.equals(entity.getAppCode(), oldEntity.getAppCode())) {
            List<SysPermission> childrenList = new ArrayList<>();
            findAllChildrenPermission(entity.getId(), childrenList);
            if (CollectionUtil.isNotEmpty(childrenList)) {
                for (SysPermission permission : childrenList) {
                    // 更新子菜单所属子系统
                    permission.setAppCode(entity.getAppCode());
                    sysPermissionMapper.updateById(permission);
                }
            }
        }

        //对象属性复制
        PojoUtils.copyPropertiesIgnoreNull(entity, oldEntity);
        sysPermissionMapper.updateById(oldEntity);
        return oldEntity;
    }

    /**
     * 查找所有子权限（包括子权限的子权限）
     * @param id    父权限Id
     * @param permissionList    子权限集合
     */
    public void findAllChildrenPermission(String id, List<SysPermission> permissionList) {
        List<SysPermission> childrenList = sysPermissionMapper.selectList(Wrappers.<SysPermission>lambdaQuery().eq(SysPermission::getParentId, id));
        if (CollectionUtil.isNotEmpty(childrenList)) {
            permissionList.addAll(childrenList);
            for (SysPermission permission : permissionList) {
                findAllChildrenPermission(permission.getId(), permissionList);
            }
        }
    }

    /**
     * 删除权限实体类对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean del(String ids) {
        String[] idArray = ids.split(",");
        for (String permissionId : idArray) {
            // 删除角色关联
            rolePermissionMappingMapper.delete(Wrappers.<RolePermissionMapping>lambdaQuery().eq(RolePermissionMapping::getPermissionId, permissionId));
            // 删除上下级关联
            sysPermissionMapper.delete(Wrappers.<SysPermission>lambdaQuery().eq(SysPermission::getParentId, permissionId));
        }
        sysPermissionMapper.deleteBatchIds(CollectionUtil.toList(idArray));
        return true;
    }

    /**
     * 更新角色权限关联
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRolePermission(UpdateRolePermissionForm form) {
        String roleId = form.getRoleId();
        // 清除原有关联
        rolePermissionMappingMapper.delete(Wrappers.<RolePermissionMapping>lambdaQuery().eq(RolePermissionMapping::getRoleId, roleId));
        if (StrUtil.isBlank(form.getLastPermissionIds())) {
            return;
        }
        // 建立新关联
        String[] permissionIdArray = form.getLastPermissionIds().split(",");
        for (String permissionId : permissionIdArray) {
            RolePermissionMapping mapping = new RolePermissionMapping();
            mapping.setId(IdGenerateHelper.nextId());
            mapping.setRoleId(roleId);
            mapping.setPermissionId(permissionId);
            rolePermissionMappingMapper.insert(mapping);
        }
    }

    /**
     * 查询角色权限关联
     * @param roleId    角色ID
     * @return 权限ID数组
     */
    @Override
    public List<String> queryRolePermission(String roleId) {
        List<RolePermissionMapping> mappings = rolePermissionMappingMapper.selectList(Wrappers.<RolePermissionMapping>lambdaQuery().eq(RolePermissionMapping::getRoleId, roleId));
        if (CollectionUtil.isEmpty(mappings)) {
            return null;
        }
        List<String> permissionIdList = new ArrayList<String>(mappings.size());
        for (RolePermissionMapping mapping : mappings) {
            permissionIdList.add(mapping.getPermissionId());
        }
        return permissionIdList;
    }

    /**
     * 查询所有权限（树图关联）
     * @return  权限列表
     */
    @Override
    public List<SysPermission> queryTreeList() {
        List<SysPermission> result = new LinkedList<>();
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(SysPermission::getSortNo);
        List<SysPermission> permissionList = sysPermissionMapper.selectList(queryWrapper);
        return linkTreeNode(permissionList);
    }

    /**
     * 查询所有可选权限（树图关联）
     * @return  权限列表
     */
    @Override
    public List<SysPermission> selectTreeList() {
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysPermission::getStatus, BaseConstants.STATUS_NOMAL).orderByAsc(SysPermission::getSortNo);
        List<SysPermission> permissionList = sysPermissionMapper.selectList(queryWrapper);

        return linkTreeNode(permissionList);
    }


    /**
     * 串联树结构
     * @param permissionList    待串联的权限列表
     */
    public static List<SysPermission> linkTreeNode(List<SysPermission> permissionList) {
        List<SysPermission> result = new LinkedList<>();
        for (SysPermission permission : permissionList) {
            // 顶级权限
            if (StrUtil.isBlank(permission.getParentId())) {
                result.add(permission);
            }
        }
        // 串联树结构
        HashMap<String, SysPermission> permissionIdMap = new HashMap<>();
        for (SysPermission permission : permissionList) {
            permissionIdMap.put(permission.getId(), permission);
        }
        for (SysPermission permission : permissionList) {
            if (StrUtil.isNotBlank(permission.getParentId())) {
                SysPermission parent = permissionIdMap.get(permission.getParentId());
                if (parent != null) {
                    List<SysPermission> children = parent.getChildren();
                    if (children == null) {
                        children = new LinkedList<>();
                        parent.setChildren(children);
                    }
                    children.add(permission);
                }
            }
        }
        return result;
    }

    /**
     * 查询用户菜单
     * @return
     */
    @Override
    public List<SysPermission> queryUserMenu() {
        List<SysPermission> permissionList = null;
        // 管理员获取全部菜单
        if (JwtUtil.checkAdmin()) {
            permissionList = sysPermissionMapper.selectList(null);
        }
        else {
            // 普通用户菜单
            String userId = JwtUtil.getUserId();
            permissionList = sysPermissionMapper.selectBySysUserId(userId);
        }

        HashMap<String, SysPermission> menuMap = new HashMap<>();
        LinkedList<SysPermission> menuList = new LinkedList<>();
        if (CollectionUtil.isNotEmpty(permissionList)) {
            for (SysPermission permission : permissionList) {
                menuMap.putIfAbsent(permission.getId(), permission);
            }
            for (SysPermission permission : menuMap.values()) {
                // 添加顶级菜单
                if (BaseConstants.PERMISSION_MENU_TYPE_TOP_MENU.equals(permission.getMenuType())) {
                    menuList.add(permission);
                }

                // 附加子菜单和权限
                if (StrUtil.isNotEmpty(permission.getParentId())) {
                    SysPermission parent = menuMap.get(permission.getParentId());
                    if (parent == null) {
                        continue;
                    }
                    List<SysPermission> children = parent.getChildren();
                    if (children == null) {
                        children = new LinkedList<>();
                        parent.setChildren(children);
                    }
                    children.add(permission);
                }
            }
        }

        // 菜单排序比较器
        Comparator<SysPermission> comparator = new Comparator<SysPermission>() {
            public int compare(SysPermission s1, SysPermission s2) {
                if (s1.getSortNo() != s2.getSortNo()) {
                    return s1.getSortNo() - s2.getSortNo();
                } else {
                    return 0;
                }
            }
        };

        // 菜单排序
        menuSort(comparator, menuList);
        return menuList;
    }

    /**
     * 菜单（子菜单）重排序
     * @param list
     */
    public static void menuSort(Comparator comparator, List<SysPermission> list) {
        //根据规则进行排序
        Collections.sort(list, comparator);
        for (SysPermission permission : list) {
            List<SysPermission> children = permission.getChildren();
            if (CollectionUtil.isNotEmpty(children)) {
                menuSort(comparator, children);
            }
        }
    }
}
