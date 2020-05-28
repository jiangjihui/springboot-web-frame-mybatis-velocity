package com.jjh.business.system.support.controller;


import com.jjh.business.system.support.controller.form.SysDictTypeQueryForm;
import com.jjh.business.system.support.model.SysDictType;
import com.jjh.business.system.support.service.SysDictTypeService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.PageRequestForm;
import com.jjh.common.web.form.PageResponseForm;
import com.jjh.common.web.form.SimpleResponseForm;
import com.jjh.framework.plugin.excel.ExcelUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
*   字典类型管理
 * @author jjh
 * @date 2019/12/09
*/
@Api(tags = "[a1060]字典类型管理")
@RestController
@RequestMapping("/system/support/sys_dict_type")
public class SysDictTypeController extends BaseController {

    @Autowired
    private SysDictTypeService sysDictTypeService;


    /**
     * 字典类型列表
     */
    @ApiOperation("字典类型列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysDictType>> list(@RequestBody PageRequestForm<SysDictTypeQueryForm> form) {
        List<SysDictType> list = sysDictTypeService.list(form);
        return page(form, list);
    }

    /**
     * 新增字典类型
     */
    @ApiOperation("新增字典类型")
    @ApiOperationSupport(ignoreParameters = {"id","createTime","updateTime","createBy","updateBy","dictData"})
    @PostMapping("/add")
    public SimpleResponseForm<SysDictType> add(HttpServletRequest request, @Valid @RequestBody SysDictType entity) {
        SysDictType result = sysDictTypeService.add(entity);
        return success(result);
    }

    /**
     * 更新字典类型
     */
    @ApiOperation("更新字典类型")
    @ApiOperationSupport(ignoreParameters = {"createTime","updateTime","createBy","updateBy","dictData"})
    @PostMapping("/update")
    public SimpleResponseForm<SysDictType> update(HttpServletRequest request, @Valid @RequestBody SysDictType entity) {
        SysDictType result = sysDictTypeService.update(entity);
        return success(result);
    }

    /**
     * 删除字典类型
     */
    @ApiOperation("删除字典类型")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysDictTypeService.del(ids);
        return success();
    }

    /**
     * 字典类型导入模板
     * @return
     */
    @ApiOperation(value = "字典类型导入模板")
    @GetMapping("/import_template")
    public SimpleResponseForm<String> importTemplate() {
        ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
        return success(util.importTemplateExcel( "字典类型导入"));
    }

    /**
     * 字典类型导入
     * @return
     */
    @ApiOperation(value = "字典类型导入")
    @PostMapping("/import_data")
    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
        if (file != null) {
            ExcelUtil<SysDictType> util = new ExcelUtil<>(SysDictType.class);
            List<SysDictType> list = util.importExcel(file);
                sysDictTypeService.importData(list, updateSupport);
        }
        return success();
    }

    /**
     * 字典类型导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("字典类型导出")
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<SysDictTypeQueryForm> form) {
        List<SysDictType> list = sysDictTypeService.list(form);
        ExcelUtil<SysDictType> excelUtil = new ExcelUtil<>(SysDictType.class);
        return success(excelUtil.exportExcel(list, "字典类型列表"));
    }

    /**
     * 查询所有字典类型以及其对应的字典值
     */
    @ApiOperation("查询所有字典类型以及其对应的字典值")
    @GetMapping("/list_all_type_and_data")
    public SimpleResponseForm<List<SysDictType>> listAllTypeAndData() {
        return success(sysDictTypeService.listAllTypeAndData());
    }
}
