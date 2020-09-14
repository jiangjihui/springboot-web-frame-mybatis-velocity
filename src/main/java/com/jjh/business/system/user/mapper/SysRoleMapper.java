package com.jjh.business.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjh.business.system.user.model.SysRole;

import java.util.List;

/**
 * 角色实体类 数据层
 * 
 * @author jjh
 * @date 2020/02/13
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     *  获取角色信息
     * @param userId    用户ID
     */
    List<SysRole> listSysRole(String userId);

    /**
     *  获取角色信息
     * @param userId    用户ID
     */
    Integer hasRole(String userId, String name);

    /**
     *  获取角色Code
     * @param userId    用户ID
     */
    List<String> listSysRoleCode(String userId);

    /**
     *  获取角色Code
     * @param userId    用户ID
     * @param status    角色状态
     */
    List<String> listSysRoleCodeByStatus(String userId, Integer status);

}