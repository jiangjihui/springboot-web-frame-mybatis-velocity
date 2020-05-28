package com.jjh.business.system.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjh.business.system.user.model.SysPermission;

import java.util.List;

/**
 * 权限实体类 数据层
 * 
 * @author jjh
 * @date 2020/02/13
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 获取用户的所有权限
     * @param userId    用户ID
     * @return
     */
    List<SysPermission> selectBySysUserId(String userId);

    /**
     *  获取权限Code
     * @param id    用户ID
     */
    List<String> listSysPermissionCode(String id);

    /**
     * 获取所有权限code
     * @return
     */
    List<String> listAllSysPermissionCode();
}