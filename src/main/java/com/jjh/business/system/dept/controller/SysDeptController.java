package com.jjh.business.system.dept.controller;


import com.jjh.business.system.dept.model.SysDept;
import com.jjh.business.system.dept.service.SysDeptService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import com.jjh.framework.aspect.operationlog.BusinessType;
import com.jjh.framework.aspect.operationlog.Log;
import com.jjh.framework.plugin.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
*   部门管理
 *
 * @author jjh
 * @date 2020/04/21
*/
@Api(tags = "[a3010]部门管理")
@RestController
@RequestMapping("/system/dept/sys_dept")
public class SysDeptController extends BaseController {

    @Autowired
    private SysDeptService sysDeptService;


    /**
     * 部门列表
     */
    @ApiOperation("部门列表")
    @ApiOperationSupport(order = 1)
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysDept>> list(@RequestBody PageRequestForm<SysDept> form) {
        List<SysDept> list = sysDeptService.list(form);
        return page(form, list);
    }

    /**
     * 新增部门
     */
    @Log(title = "新增部门", businessType = BusinessType.INSERT)
    @ApiOperation("新增部门")
    @ApiOperationSupport(order = 2, ignoreParameters = {"id","createTime","updateTime","createBy","updateBy","children"})
    @PostMapping("/add")
    public SimpleResponseForm<SysDept> add(@Valid @RequestBody SysDept entity) {
        SysDept result = sysDeptService.add(entity);
        return success();
    }

    /**
     * 更新部门
     */
    @ApiOperation("更新部门")
    @ApiOperationSupport(order = 3, ignoreParameters = {"createTime","updateTime","createBy","updateBy","children"})
    @PostMapping("/update")
    public SimpleResponseForm<SysDept> update(@Valid @RequestBody SysDept entity) {
        SysDept result = sysDeptService.update(entity);
        return success();
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门")
    @ApiOperationSupport(order = 4)
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysDeptService.del(ids);
        return success();
    }

    /**
     * 部门导入模板
     * @return
     */
    @ApiOperation(value = "部门导入模板")
    @ApiOperationSupport(order = 5)
    @GetMapping("/import_template")
    public SimpleResponseForm<String> importTemplate() {
        ExcelUtil<SysDept> util = new ExcelUtil<>(SysDept.class);
        return success(util.importTemplateExcel( "部门导入"));
    }

    /**
     * 部门导入
     * @return
     */
    @ApiOperation(value = "部门导入")
    @ApiOperationSupport(order = 6)
    @PostMapping("/import_data")
    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
        if (file == null) {
            return error("请选择文件");
        }
        ExcelUtil<SysDept> util = new ExcelUtil<>(SysDept.class);
        List<SysDept> list = util.importExcel(file);
        sysDeptService.importData(list, updateSupport);
        return success();
    }

    /**
     * 部门导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("部门导出")
    @ApiOperationSupport(order = 7)
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<SysDept> form) {
        List<SysDept> list = sysDeptService.list(form);
        ExcelUtil<SysDept> excelUtil = new ExcelUtil<>(SysDept.class);
        return success(excelUtil.exportExcel(list, "部门列表"));
    }

    /**
     * 查询树结构列表（选项列表）
     */
    @ApiOperation("查询树结构列表（选项列表）")
    @ApiOperationSupport(order = 8)
    @GetMapping("/select_tree_list")
    public SimpleResponseForm<List<SysDept>> selectTreeList() {
        return success(sysDeptService.selectTreeList());
    }

    /**
     * 查询所有树结构列表（管理）
     */
    @ApiOperation("查询所有树结构列表（管理）")
    @ApiOperationSupport(order = 9)
    @PostMapping("/all_tree_list")
    public SimpleResponseForm<List<SysDept>> allTreeList(@RequestBody PageRequestForm<SysDept> form) {
        return success(sysDeptService.allTreeList(form));
    }

    /**
     * 查询子部门列表
     */
    @ApiOperation("查询子部门列表")
    @ApiOperationSupport(order = 10)
    @GetMapping("/children_list")
    public SimpleResponseForm<List<SysDept>> childrenList(String deptId) {
        return success(sysDeptService.childrenList(deptId));
    }

    /**
     * 查询子部门ID列表
     */
    @ApiOperation("查询子部门ID列表")
    @ApiOperationSupport(order = 11)
    @GetMapping("/children_id_list")
    public SimpleResponseForm<List<String>> childrenIdList(String deptId) {
        return success(sysDeptService.childrenIdList(deptId));
    }
}
