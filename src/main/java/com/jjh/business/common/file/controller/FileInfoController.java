package com.jjh.business.common.file.controller;

import com.jjh.business.common.file.model.FileInfo;
import com.jjh.business.common.file.service.FileInfoService;
import com.jjh.common.web.controller.BaseController;
import com.jjh.common.web.form.SimpleResponseForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  文件处理
 *
 * @author jjh
 * @date 2019/11/19
 */
@Api(tags = "[a]文件处理")
@Controller
@RequestMapping("/common")
public class FileInfoController extends BaseController {

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * 上传文件
     * @param file 上传的文件
     */
    @ApiOperation(value = "文件上传",notes = "通用上传请求")
    @PostMapping("/upload")
    @ResponseBody
    public SimpleResponseForm<FileInfo> uploadFile(MultipartFile file) {
        FileInfo userFileInfo = fileInfoService.uploadFile(file);
        return success(userFileInfo);
    }

    /**
     * 上传文件
     * @param file 上传的文件
     */
    @ApiOperation(value = "静态文件上传",notes = "用于上传需要在前端直接展示的文件，" +
            "比如头像，返回值为文件的链接地址，示例：" +
            "http://localhost:8084/api/static/7ad965f3-c807-4319-bf62-8cf23519ffa4.jpg")
    @PostMapping("/upload_static_file")
    @ResponseBody
    public SimpleResponseForm<String> uploadStaticFile(MultipartFile file) {
        String fileName = fileInfoService.uploadStaticFile(file);
        return success(fileName);
    }


    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @ApiOperation(value = "文件下载", notes = "通用下载请求，返回待下载文件的数据流")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "fileName", dataType = "String", required = true, value = "文件名称"),
            @ApiImplicitParam(paramType = "query", name = "delete", dataType = "Boolean", required = true, value = "是否删除")
    })
    @GetMapping(value = "/download", produces = "application/octet-stream")
    public void downloadFile(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        fileInfoService.downloadFile(fileName, delete, response, request);
    }
}
