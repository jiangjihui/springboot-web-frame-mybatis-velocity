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
     *  获取角色Code
     * @param id    用户ID
     */
    List<String> listSysRoleCode(String id);

}