package com.jjh.business.system.record.controller;


import com.jjh.business.system.record.controller.form.QueryOperationLogForm;
import com.jjh.business.system.record.model.SysOperationLog;
import com.jjh.business.system.record.service.SysOperationLogService;
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
*   操作日志管理
 *
 * @author jjh
 * @date 2020/04/26
*/
@Api(tags = "[a4010]操作日志管理")
@RestController
@RequestMapping("/system/record/sys_operation_log")
public class SysOperationLogController extends BaseController {

    @Autowired
    private SysOperationLogService sysOperationLogService;


    /**
     * 操作日志列表
     */
    @ApiOperation("操作日志列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysOperationLog>> list(@RequestBody PageRequestForm<QueryOperationLogForm> form) {
        List<SysOperationLog> list = sysOperationLogService.list(form);
        return page(form, list);
    }

    /**
     * 新增操作日志
     */
    @ApiOperation("新增操作日志")
    @PostMapping("/add")
    public SimpleResponseForm<SysOperationLog> add(@Valid @RequestBody SysOperationLog entity) {
        SysOperationLog result = sysOperationLogService.add(entity);
        return success();
    }

    /**
     * 更新操作日志
     */
    @ApiOperation("更新操作日志")
    @PostMapping("/update")
    public SimpleResponseForm<SysOperationLog> update(@Valid @RequestBody SysOperationLog entity) {
        SysOperationLog result = sysOperationLogService.update(entity);
        return success();
    }

    /**
     * 删除操作日志
     */
    @ApiOperation("删除操作日志")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysOperationLogService.del(ids);
        return success();
    }

    /**
     * 操作日志导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("操作日志导出")
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<QueryOperationLogForm> form) {
        List<SysOperationLog> list = sysOperationLogService.list(form);
        ExcelUtil<SysOperationLog> excelUtil = new ExcelUtil<>(SysOperationLog.class);
        return success(excelUtil.exportExcel(list, "操作日志列表"));
    }
}
