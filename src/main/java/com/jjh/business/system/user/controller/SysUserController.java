package com.jjh.business.system.user.controller;

import com.jjh.business.system.user.controller.form.QueryUserForm;
import com.jjh.business.system.user.controller.form.ResetCurrentUserPwdForm;
import com.jjh.business.system.user.controller.form.ResetPasswordForm;
import com.jjh.business.system.user.controller.form.UserFrozenForm;
import com.jjh.business.system.user.model.SysUser;
import com.jjh.business.system.user.service.SysUserService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import com.jjh.framework.jwt.JwtUtil;
import com.jjh.framework.plugin.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户信息
 *
 * @author jjh
 * @date 2019/11/14
 **/
@Api(tags = "[a1020]用户管理")
@RestController
@RequestMapping("/system/user/sys_user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);


    /**
     * 用户列表
     * @param form 分页请求参数
     * @return 用户列表
     */
//    @RequiresPermissions("system:sysUser:list")
    @ApiOperation("用户列表")
    @ApiOperationSupport(order = 1)
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysUser>> list(@RequestBody PageRequestForm<QueryUserForm> form) {
        List<SysUser> list = sysUserService.list(form);
        return page(form, list);
    }

    /**
     * 新增用户
     * @param entity 用户信息
     * @return 用户信息
     */
    @ApiOperation(value = "保存用户信息")
    @ApiOperationSupport(order = 2, ignoreParameters = {"id","createTime","updateTime","createBy","updateBy","roleList"})
    @PostMapping("/add")
    public SimpleResponseForm<String> add(@Valid @RequestBody SysUser entity) {
        sysUserService.add(entity);
        return success();
    }

    /**
     * 更新
     * @param entity 用户信息
     * @return 用户信息
     */
    @ApiOperation(value = "更新用户信息")
    @ApiOperationSupport(order = 3, ignoreParameters = {"createTime","updateTime","createBy","updateBy","roleList"})
    @PostMapping("/update")
    public SimpleResponseForm<String> update(@Valid @RequestBody SysUser entity) {
        sysUserService.update(entity);
        return success();
    }

    /**
     * 删除
     * @param ids 待删除的ID数组
     */
    @ApiOperation(value = "删除用户信息")
    @ApiOperationSupport(order = 4)
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysUserService.delete(ids);
        return success();
    }

    /**
     * 重置密码
     * @param form
     * @return
     */
    @ApiOperation(value = "重置密码")
    @ApiOperationSupport(order = 5)
    @PostMapping(value = "/reset")
    public SimpleResponseForm<String> reset(@Valid @RequestBody ResetPasswordForm form) {
        sysUserService.resetPassword(form);
        return success();
    }

    /**
     * 冻结/解冻
     * @param list
     * @return
     */
    @ApiOperation(value = "冻结/解冻")
    @ApiOperationSupport(order = 6)
    @PostMapping("/frozen")
    public SimpleResponseForm<String> frozen(@Valid @RequestBody List<UserFrozenForm> list) {
        sysUserService.frozen(list);
        return success();
    }

    /**
     * 用户导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("用户导出")
    @ApiOperationSupport(order = 7)
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<QueryUserForm> form) {
        List<SysUser> list = sysUserService.list(form);
        ExcelUtil<SysUser> excelUtil = new ExcelUtil<>(SysUser.class);
        return success(excelUtil.exportExcel(list, "用户列表"));
    }

    /**
     * 用户导入模板
     * @return
     */
    @ApiOperation(value = "用户导入模板")
    @ApiOperationSupport(order = 8)
    @GetMapping("/import_template")
    public SimpleResponseForm<String> importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return success(util.importTemplateExcel( "用户导入"));
    }

    /**
     * 用户数据导入
     * @return
     */
    @ApiOperation(value = "用户数据导入")
    @ApiOperationSupport(order = 9)
    @PostMapping("/import_data")
    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
        if (file != null) {
            ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
            List<SysUser> list = util.importExcel(file);
            sysUserService.importData(list, updateSupport);
        }
        return success();
    }

    /**
     * 获取用户信息
     * @param id 用户ID
     */
    @ApiOperation(value = "获取用户信息")
    @ApiOperationSupport(order = 10)
    @GetMapping("/info")
    public SimpleResponseForm<SysUser> info(String id) {
        return success(sysUserService.info(id));
    }

    /**
     * 获取当前用户信息
     */
    @ApiOperation(value = "获取当前用户信息")
    @ApiOperationSupport(order = 11)
    @GetMapping("/current_user_info")
    public SimpleResponseForm<SysUser> currentUserInfo(){
        return success(sysUserService.info(JwtUtil.getUserId()));
    }

    /**
     * 重置当前用户密码
     */
    @ApiOperation(value = "重置当前用户密码")
    @ApiOperationSupport(order = 12)
    @PostMapping("/reset_current_user")
    public SimpleResponseForm<String> resetCurrentUser(@Valid @RequestBody ResetCurrentUserPwdForm form){
        sysUserService.resetCurrentUserPwd(form);
        return success();
    }

    /**
     * 根据用户名称获取用户信息
     */
    @ApiOperation("根据用户名称获取用户列表")
    @ApiOperationSupport(order = 13)
    @ApiImplicitParam(name = "name", value = "用户名称")
    @GetMapping("/find_by_name")
    public SimpleResponseForm<List<SysUser>> findByName(String name) {
        return success(sysUserService.findByName(name));
    }

}
