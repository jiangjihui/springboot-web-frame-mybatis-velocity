package com.jjh.business.system.support.controller;


import com.jjh.business.system.support.model.SysCategory;
import com.jjh.business.system.support.service.SysCategoryService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import com.jjh.framework.plugin.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
*   分类字典管理
 * @author jjh
 * @date 2019/12/12
*/
@Api(tags = "[a1080]分类字典管理")
@RestController
@RequestMapping("/system/support/sys_category")
public class SysCategoryController extends BaseController {

    @Autowired
    private SysCategoryService sysCategoryService;


    /**
     * 分类字典列表
     */
    @ApiOperation("分类字典列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysCategory>> list(@RequestBody PageRequestForm<SysCategory> form) {
        List<SysCategory> list = sysCategoryService.list(form);
        return page(form, list);
    }

    /**
     * 新增分类字典
     */
    @ApiOperation("新增分类字典")
    @ApiOperationSupport(ignoreParameters = {"id","createTime","updateTime","createBy","updateBy"})
    @PostMapping("/add")
    public SimpleResponseForm<SysCategory> add(@Valid @RequestBody SysCategory entity) {
        SysCategory result = sysCategoryService.add(entity);
        return success(result);
    }

    /**
     * 更新分类字典
     */
    @ApiOperation("更新分类字典")
    @ApiOperationSupport(ignoreParameters = {"createTime","updateTime","createBy","updateBy"})
    @PostMapping("/update")
    public SimpleResponseForm<SysCategory> update(@Valid @RequestBody SysCategory entity) {
        SysCategory result = sysCategoryService.update(entity);
        return success(result);
    }

    /**
     * 删除分类字典
     */
    @ApiOperation("删除分类字典")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysCategoryService.del(ids);
        return success();
    }

    /**
     * 分类字典导入模板
     * @return
     */
    @ApiOperation(value = "分类字典导入模板")
    @GetMapping("/import_template")
    public SimpleResponseForm<String> importTemplate() {
        ExcelUtil<SysCategory> util = new ExcelUtil<>(SysCategory.class);
        return success(util.importTemplateExcel( "分类字典导入"));
    }

    /**
     * 分类字典导入
     * @return
     */
    @ApiOperation(value = "分类字典导入")
    @PostMapping("/import_data")
    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
        if (file != null) {
            ExcelUtil<SysCategory> util = new ExcelUtil<>(SysCategory.class);
            List<SysCategory> list = util.importExcel(file);
                sysCategoryService.importData(list, updateSupport);
        }
        return success();
    }

    /**
     * 分类字典导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("分类字典导出")
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<SysCategory> form) {
        List<SysCategory> list = sysCategoryService.list(form);
        ExcelUtil<SysCategory> excelUtil = new ExcelUtil<>(SysCategory.class);
        return success(excelUtil.exportExcel(list, "分类字典列表"));
    }

    /**
     * 顶级分类字典列表
     */
    @ApiOperation("顶级分类字典列表")
    @PostMapping("/rootList")
    public SimpleResponseForm<PageResponseForm<SysCategory>> rootList(@RequestBody PageRequestForm<SysCategory> form) {
        List<SysCategory> list = sysCategoryService.rootList(form);
        return page(form, list);
    }

    /**
     * 子分类字典列表
     */
    @ApiOperation("子分类字典列表")
    @GetMapping("/childrenList")
    public SimpleResponseForm<List<SysCategory>> childrenList(String parentId) {
        return success(sysCategoryService.childrenList(parentId));
    }

    /**
     * 查找分类字典（回显使用）
     */
    @ApiOperation("查找分类字典")
    @GetMapping("/loadOne")
    @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "分类字典编码")
    public SimpleResponseForm<SysCategory> loadOne(String code) {
        return success(sysCategoryService.getByCode(code));
    }

    /**
     * 获取分类字典树结构
     */
    @ApiOperation("获取分类字典树结构")
    @GetMapping("/loadTree")
    @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "分类字典编码")
    public SimpleResponseForm<SysCategory> loadTree(@Valid @NotBlank(message = "分类字典编码不能为空") String code) {
        return success(sysCategoryService.loadTree(code));
    }

}
