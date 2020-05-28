package com.jjh.business.system.user.controller;


import com.jjh.business.system.user.controller.form.UpdateRolePermissionForm;
import com.jjh.business.system.user.model.SysPermission;
import com.jjh.business.system.user.service.SysPermissionService;
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
*   权限管理
 * @author jjh
 * @date 2019/11/20
*/
@Api(tags = "[a1040]权限管理")
@RestController
@RequestMapping("/system/user/sys_permission")
public class SysPermissionController extends BaseController {

    @Autowired
    private SysPermissionService sysPermissionService;


    /**
     * 权限列表
     */
    @ApiOperation("权限列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysPermission>> list(@RequestBody PageRequestForm<SysPermission> form) {
        List<SysPermission> list = sysPermissionService.list(form);
        return page(form, list);
    }

    /**
     * 新增权限
     */
    @ApiOperation("新增权限")
    @ApiOperationSupport(ignoreParameters = {"id","createTime","updateTime","createBy","updateBy","children"})
    @PostMapping("/add")
    public SimpleResponseForm<SysPermission> add(@Valid @RequestBody SysPermission entity) {
        SysPermission result = sysPermissionService.add(entity);
        return success(result);
    }

    /**
     * 更新权限
     */
    @ApiOperation("更新权限")
    @ApiOperationSupport(ignoreParameters = {"createTime","updateTime","createBy","updateBy","children"})
    @PostMapping("/update")
    public SimpleResponseForm<SysPermission> update(@Valid @RequestBody SysPermission entity) {
        SysPermission result = sysPermissionService.update(entity);
        return success(result);
    }

    /**
     * 删除权限
     */
    @ApiOperation("删除权限")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysPermissionService.del(ids);
        return success();
    }

    /**
     * 更新角色权限关联
     */
    @ApiOperation("更新角色权限关联")
    @PostMapping("/update_role_permission")
    public SimpleResponseForm<String> updateRolePermission(@RequestBody UpdateRolePermissionForm form) {
        sysPermissionService.updateRolePermission(form);
        return success();
    }

    /**
     * 查询用户菜单
     */
    @ApiOperation("查询用户菜单")
    @GetMapping("/query_user_menu")
    public SimpleResponseForm<List<SysPermission>> queryUserMenu() {
        return success(sysPermissionService.queryUserMenu());
    }

    /**
     * 查询角色权限关联
     */
    @ApiOperation("查询角色权限关联")
    @GetMapping("/query_role_permission")
    public SimpleResponseForm<List<String>> queryRolePermission(String roleId) {
        return success(sysPermissionService.queryRolePermission(roleId));
    }

    /**
     * 查询所有权限（树图关联）
     */
    @ApiOperation("查询所有权限（树图关联）")
    @GetMapping("/query_tree_list")
    public SimpleResponseForm<List<SysPermission>> queryTreeList() {
        return success(sysPermissionService.queryTreeList());
    }

    /**
     * 查询所有可选权限（树图关联）
     */
    @ApiOperation("查询所有可选权限（树图关联）")
    @GetMapping("/select_tree_list")
    public SimpleResponseForm<List<SysPermission>> selectTreeList() {
        return success(sysPermissionService.selectTreeList());
    }
}
