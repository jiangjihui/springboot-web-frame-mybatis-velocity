package com.business.common.gen.controller;

import com.business.common.gen.service.IGenSerivce;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 代码生成 操作处理
 *
 */
@Controller
@RequestMapping("/gen")
public class GenController {

    @Autowired
    private IGenSerivce genSerivce;

    /**
     * 代码生成 controller/service/dao
     * 示例：localhost:8088/gen/genCode/sys_notice
     * @param response
     * @param tableName
     * @throws IOException
     */
    @GetMapping("/genCode/{tableName}")
    public void genCode(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException {
        byte[] data = genSerivce.generatorCode(tableName);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"demoCode.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

}
