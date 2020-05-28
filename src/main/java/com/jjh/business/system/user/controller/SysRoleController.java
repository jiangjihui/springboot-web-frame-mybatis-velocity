package com.jjh.business.system.user.controller;


import com.jjh.business.system.user.controller.form.QueryRoleForm;
import com.jjh.business.system.user.controller.form.UpdateUserRoleForm;
import com.jjh.business.system.user.model.SysRole;
import com.jjh.business.system.user.service.SysRoleService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
*   角色管理
 * @author jjh
 * @date 2019/11/20
*/
@Api(tags = "[a1030]角色管理")
@RestController
@RequestMapping("/system/user/sys_role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService sysRoleService;


    /**
     * 角色列表
     */
    @ApiOperation("角色列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysRole>> list(@RequestBody PageRequestForm<QueryRoleForm> form) {
        List<SysRole> list = sysRoleService.list(form);
        return page(form, list);
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @ApiOperationSupport(ignoreParameters = {"id","createTime","updateTime","createBy","updateBy","permissionList"})
    @PostMapping("/add")
    public SimpleResponseForm<SysRole> add(@Valid @RequestBody SysRole entity) {
        SysRole result = sysRoleService.add(entity);
        return success(result);
    }

    /**
     * 更新角色
     */
    @ApiOperation("更新角色")
    @ApiOperationSupport(ignoreParameters = {"createTime","updateTime","createBy","updateBy","permissionList"})
    @PostMapping("/update")
    public SimpleResponseForm<SysRole> update(@Valid @RequestBody SysRole entity) {
        SysRole result = sysRoleService.update(entity);
        return success(result);
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysRoleService.del(ids);
        return success();
    }


    /**
     * 更新用户角色关联
     */
    @ApiOperation("更新用户角色关联")
    @PostMapping("/update_user_role")
    public SimpleResponseForm<String> updateUserRole(UpdateUserRoleForm form) {
        sysRoleService.updateUserRole(form);
        return success();
    }

    /**
     * 查询用户角色关联
     */
    @ApiOperation("查询用户角色关联")
    @GetMapping("/query_user_role")
    public SimpleResponseForm<List<String>> queryUserRole(String userId) {
        return success(sysRoleService.queryUserRole(userId));
    }

    /**
     * 查询所有角色
     */
    @ApiOperation("查询所有角色")
    @GetMapping("/query_all")
    public SimpleResponseForm<List<SysRole>> queryAll() {
        return success(sysRoleService.queryAll());
    }
}
