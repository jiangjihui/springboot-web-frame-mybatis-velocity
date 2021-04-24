package com.jjh.business.common.file.service;

import com.jjh.business.common.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  文件服务
 *
 * @author jjh
 * @date 2019/11/19
 */
public interface FileInfoService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    FileInfo uploadFile(MultipartFile file);

    /**
     * 上传静态文件
     * @param file
     * @return
     */
    String uploadStaticFile(MultipartFile file);

    /**
     * 下载文件
     * @param fileName 文件名称
     * @param delete 是否删除
     * @param response 响应
     * @param request 请求
     */
    void downloadFile(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request);

    /**
     * 写入文件到响应流
     * @param filePath 文件路径
     * @param delete 是否删除
     * @param response 响应
     * @param request 请求
     */
    void writeFile(String filePath, Boolean delete, HttpServletResponse response, HttpServletRequest request);
}
