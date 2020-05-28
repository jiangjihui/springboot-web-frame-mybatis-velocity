package com.jjh.business.system.tenant.controller;


import com.jjh.business.system.tenant.model.SysApplication;
import com.jjh.business.system.tenant.service.SysApplicationService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import com.jjh.framework.plugin.excel.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
*   子系统管理
 *
 * @author jjh
 * @date 2020/02/18
*/
@Api(tags = "[a2010]子系统管理")
@RestController
@RequestMapping("/system/tenant/sys_application")
public class SysApplicationController extends BaseController {

    @Autowired
    private SysApplicationService sysApplicationService;


    /**
     * 子系统列表
     */
    @ApiOperation("子系统列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysApplication>> list(@RequestBody PageRequestForm<SysApplication> form) {
        List<SysApplication> list = sysApplicationService.list(form);
        return page(form, list);
    }

    /**
     * 新增子系统
     */
    @ApiOperation("新增子系统")
    @PostMapping("/add")
    public SimpleResponseForm<SysApplication> add(@Valid @RequestBody SysApplication entity) {
        SysApplication result = sysApplicationService.add(entity);
        return success();
    }

    /**
     * 更新子系统
     */
    @ApiOperation("更新子系统")
    @PostMapping("/update")
    public SimpleResponseForm<SysApplication> update(@Valid @RequestBody SysApplication entity) {
        SysApplication result = sysApplicationService.update(entity);
        return success();
    }

    /**
     * 删除子系统
     */
    @ApiOperation("删除子系统")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysApplicationService.del(ids);
        return success();
    }

    /**
     * 子系统导入模板
     * @return
     */
//    @ApiOperation(value = "子系统导入模板")
//    @GetMapping("/import_template")
//    public SimpleResponseForm<String> importTemplate() {
//        ExcelUtil<SysApplication> util = new ExcelUtil<>(SysApplication.class);
//        return success(util.importTemplateExcel( "子系统导入"));
//    }

    /**
     * 子系统导入
     * @return
     */
//    @ApiOperation(value = "子系统导入")
//    @PostMapping("/import_data")
//    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
//        if (file != null) {
//            ExcelUtil<SysApplication> util = new ExcelUtil<>(SysApplication.class);
//            List<SysApplication> list = util.importExcel(file);
//                sysApplicationService.importData(list, updateSupport);
//        }
//        return success();
//    }

    /**
     * 子系统导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("子系统导出")
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<SysApplication> form) {
        List<SysApplication> list = sysApplicationService.list(form);
        ExcelUtil<SysApplication> excelUtil = new ExcelUtil<>(SysApplication.class);
        return success(excelUtil.exportExcel(list, "子系统列表"));
    }


    /**
     * 查询所有子系统
     */
    @ApiOperation("查询所有子系统")
    @GetMapping("/query_all")
    public SimpleResponseForm<List<SysApplication>> queryAll() {
        return success(sysApplicationService.queryAll());
    }

}
