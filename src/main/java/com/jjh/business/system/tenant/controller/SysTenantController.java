package com.jjh.business.system.tenant.controller;


import cn.hutool.core.util.RandomUtil;
import com.jjh.business.system.tenant.controller.form.DistributionApplicationForm;
import com.jjh.business.system.tenant.model.SysTenant;
import com.jjh.business.system.tenant.service.SysTenantService;
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
*   租户信息管理
 *
 * @author jjh
 * @date 2020/02/18
*/
@Api(tags = "[a2020]租户信息管理")
@RestController
@RequestMapping("/system/tenant/sys_tenant")
public class SysTenantController extends BaseController {

    @Autowired
    private SysTenantService sysTenantService;


    /**
     * 租户信息列表
     */
    @ApiOperation("租户信息列表")
    @PostMapping("/list")
    public SimpleResponseForm<PageResponseForm<SysTenant>> list(@RequestBody PageRequestForm<SysTenant> form) {
        List<SysTenant> list = sysTenantService.list(form);
        return page(form, list);
    }

    /**
     * 新增租户信息
     */
    @ApiOperation("新增租户信息")
    @PostMapping("/add")
    public SimpleResponseForm<SysTenant> add(@Valid @RequestBody SysTenant entity) {
        SysTenant result = sysTenantService.add(entity);
        return success();
    }

    /**
     * 更新租户信息
     */
    @ApiOperation("更新租户信息")
    @PostMapping("/update")
    public SimpleResponseForm<SysTenant> update(@Valid @RequestBody SysTenant entity) {
        SysTenant result = sysTenantService.update(entity);
        return success();
    }

    /**
     * 删除租户信息
     */
    @ApiOperation("删除租户信息")
    @GetMapping("/delete")
    public SimpleResponseForm<String> delete(String ids) {
        sysTenantService.del(ids);
        return success();
    }

    /**
     * 租户信息导入模板
     * @return
     */
//    @ApiOperation(value = "租户信息导入模板")
//    @GetMapping("/import_template")
//    public SimpleResponseForm<String> importTemplate() {
//        ExcelUtil<SysTenant> util = new ExcelUtil<>(SysTenant.class);
//        return success(util.importTemplateExcel( "租户信息导入"));
//    }

    /**
     * 租户信息导入
     * @return
     */
//    @ApiOperation(value = "租户信息导入")
//    @PostMapping("/import_data")
//    public SimpleResponseForm<String> importData(MultipartFile file, boolean updateSupport) {
//        if (file != null) {
//            ExcelUtil<SysTenant> util = new ExcelUtil<>(SysTenant.class);
//            List<SysTenant> list = util.importExcel(file);
//                sysTenantService.importData(list, updateSupport);
//        }
//        return success();
//    }

    /**
     * 租户信息导出
     * @param form 分页请求参数
     * @return 导出文件
     */
    @ApiOperation("租户信息导出")
    @PostMapping("/export")
    public SimpleResponseForm<String> export(@RequestBody PageRequestForm<SysTenant> form) {
        List<SysTenant> list = sysTenantService.list(form);
        ExcelUtil<SysTenant> excelUtil = new ExcelUtil<>(SysTenant.class);
        return success(excelUtil.exportExcel(list, "租户信息列表"));
    }


    /**
     * 生成租户编码
     * @return
     */
    @ApiOperation(value = "生成租户编码")
    @GetMapping("/tenant_code")
    public SimpleResponseForm<String> tenantCode() {
        return success(RandomUtil.randomString(8));
    }

    /**
     * 获取租户子系统
     */
    @ApiOperation(value = "获取租户子系统", notes = "参数为租户编码，返回值为子系统编码")
    @GetMapping("/find_distribution_application")
    public SimpleResponseForm<List<String>> findDistributionApplication(String tenantCode) {
        return success(sysTenantService.findDistributionApplication(tenantCode));
    }

    /**
     * 租户分配子系统
     */
    @ApiOperation("租户分配子系统")
    @PostMapping("/distribution_application")
    public SimpleResponseForm<String> distributionApplication(@Valid @RequestBody DistributionApplicationForm form) {
        sysTenantService.distributionApplication(form);
        return success();
    }
}
