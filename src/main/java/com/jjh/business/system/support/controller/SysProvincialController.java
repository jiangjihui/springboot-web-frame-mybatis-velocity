package com.jjh.business.system.support.controller;


import com.jjh.business.system.support.model.SysProvincial;
import com.jjh.business.system.support.service.SysProvincialService;
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
import java.util.List;

/**
*   地区省份管理
 * @author jjh
 * @date 2019/12/13
*/
@Api(tags = "[a1090]地区省份管理")
@RestController
@RequestMapping("/system/support/sys_provincial")
public class SysProvincialController extends BaseController {

    @Autowired
    private SysProvincialService sysProvincialService;


    /**
     * 地区省份列表
     */
    @ApiOperation("地区省份列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysProvincial>> list(@RequestBody PageRequestForm<SysProvincial> form) {
        List<SysProvincial> list = sysProvincialService.list(form);
        return page(form, list);
    }

    /**
     * 新增地区省份
     */
    @ApiOperation("新增地区省份")
    @ApiOperationSupport(ignoreParameters = {"id","createTime","updateTime","createBy","updateBy"})
    @PostMapping("/add")
    public SimpleResponseForm<SysProvincial> add(@Valid @RequestBody SysProvincial entity) {
        SysProvincial result = sysProvincialService.add(entity);
        return success(result);
    }

    /**
     * 更新地区省份
     */
    @ApiOperation("更新地区省份")
    @ApiOperationSupport(ignoreParameters = {"createTime","updateTime","createBy","updateBy"})
    @PostMapping("/update")
    public SimpleResponseForm<SysProvincial> update(@Valid @RequestBody SysProvincial entity) {
        SysProvincial result = sysProvincialService.update(entity);
        return success(result);
    }

    /**
     * 删除地区省份
     */
    @ApiOperation("删除地区省份")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysProvincialService.del(ids);
        return success();
    }

    /**
     * 地区省份导入模板
     * @return
     */
    @ApiOperation(value = "地区省份导入模板")
    @GetMapping("/import_template")
    public SimpleResponseForm<String> importTemplate() {
        ExcelUtil<SysProvincial> util = new ExcelUtil<>(SysProvincial.class);
        return success(util.importTemplateExcel( "地区省份导入"));
    }

    /**
     * 地区省份导入
     * @return
     */
    @ApiOperation(value = "地区省份导入")
    @PostMapping("/import_data")
    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
        if (file != null) {
            ExcelUtil<SysProvincial> util = new ExcelUtil<>(SysProvincial.class);
            List<SysProvincial> list = util.importExcel(file);
                sysProvincialService.importData(list, updateSupport);
        }
        return success();
    }

    /**
     * 地区省份导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("地区省份导出")
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<SysProvincial> form) {
        List<SysProvincial> list = sysProvincialService.list(form);
        ExcelUtil<SysProvincial> excelUtil = new ExcelUtil<>(SysProvincial.class);
        return success(excelUtil.exportExcel(list, "地区省份列表"));
    }

    /**
     * 获取地区省份根节点列表
     */
    @ApiOperation("获取地区省份根节点列表")
    @GetMapping("/rootList")
    public SimpleResponseForm<List<SysProvincial>> treeList() {
        return success(sysProvincialService.rootList());
    }

    /**
     * 获取子节点列表
     */
    @ApiOperation("获取子节点列表")
    @GetMapping("/childrenList")
    @ApiImplicitParam(paramType = "query", name = "parentCode", dataType = "Integer", required = true, value = "父节点编号")
    public SimpleResponseForm<List<SysProvincial>> childrenList(Integer parentCode) {
        return success(sysProvincialService.childrenList(parentCode));
    }

    /**
     * 获取父节点
     */
    @ApiOperation("获取父节点")
    @GetMapping("/parent")
    @ApiImplicitParam(paramType = "query", name = "code", dataType = "Integer", required = true, value = "节点编号")
    public SimpleResponseForm<SysProvincial> parent(Integer code) {
        return success(sysProvincialService.parent(code));
    }
}
