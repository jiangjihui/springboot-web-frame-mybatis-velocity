package com.jjh.business.common.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.jjh.business.common.file.mapper.FileInfoMapper;
import com.jjh.business.common.file.model.FileInfo;
import com.jjh.business.common.file.service.FileInfoService;
import com.jjh.common.exception.BusinessException;
import com.jjh.common.util.IdGenerateHelper;
import com.jjh.framework.properties.FileProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 *  文件服务实现
 *
 * @author jjh
 * @date 2019/11/19
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    @Resource
    private FileInfoMapper fileInfoMapper;

    private static final Logger logger= LoggerFactory.getLogger(FileInfoServiceImpl.class);

    /**
     * 上传文件
     * @param file
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileInfo uploadFile(MultipartFile file) {
        checkUploadFile(file);

        // 上传的文件名称
        String uploadFileName = file.getOriginalFilename();
        // 保存的文件名称（重命名文件，避免重名覆盖）
        String fileKey = UUID.randomUUID().toString() + "." + FileUtil.extName(uploadFileName);

        // 上传的文件路径
        String fileName = FileProperties.getResourcePath() + File.separator + fileKey;

        File destFile = initFile(fileName);
        try {
            // 将文件从缓存中复制到上传目录
            file.transferTo(destFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("存储文件失败！", e);
            throw new BusinessException("存储文件失败");
        }

        FileInfo fileInfo = new FileInfo();
        fileInfo.setId(IdGenerateHelper.nextId());
        fileInfo.setFileSize(FileUtil.size(destFile));
        fileInfo.setFileMd5(DigestUtil.md5Hex(destFile));
        fileInfo.setFileKey(fileKey);
        if(StrUtil.isNotEmpty(uploadFileName)) {
            fileInfo.setFilename(uploadFileName);
        }
        fileInfoMapper.insert(fileInfo);
        return fileInfo;
    }

    /**
     * 上传静态文件
     * @param file
     * @return
     */
    @Override
    public String uploadStaticFile(MultipartFile file) {
        checkUploadFile(file);

        // 上传的文件名称
        String uploadFileName = file.getOriginalFilename();
        // 保存的文件名称（重命名文件，避免重名覆盖）
        String fileKey = UUID.randomUUID().toString() + "." + FileUtil.extName(uploadFileName);

        // 上传的文件路径
        String fileName = fileKey;
        String fileNamePath = FileProperties.getStaticPath() + File.separator + fileName;

        File destFile = initFile(fileNamePath);
        try {
            // 将文件从缓存中复制到上传目录
            file.transferTo(destFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("存储文件失败！", e);
            throw new BusinessException("存储文件失败");
        }
        String downLoadPath = FileProperties.getStaticUrl() + fileName;
        return downLoadPath;
    }

    /**
     * 下载文件
     * @param fileName 文件名称
     * @param delete 是否删除
     * @param response 响应
     * @param request 请求
     */
    @Override
    public void downloadFile(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        String realFileName = fileName;
        if (!checkAllowDownload(realFileName)) {
            throw new BusinessException(StrUtil.format("文件名称 {} 非法，禁止下载，请检查", realFileName));
        }
        // 下载的文件路径
        String filePath = FileProperties.getResourcePath() + File.separator + fileName;
        writeFile(filePath, delete, response, request);
    }

    /**
     * 写入文件到响应流
     * @param filePath 文件路径
     * @param delete 是否删除
     * @param response 响应
     * @param request 请求
     */
    @Override
    public void writeFile(String filePath, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new BusinessException("文件不存在，请检查");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/dto-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + setFileDownloadHeader(request, file.getName()));
        try {
            writeBytes(filePath, response.getOutputStream());
        } catch (IOException e) {
            throw new BusinessException("文件写入输出流失败", e);
        }

        if (Boolean.TRUE.equals(delete)){
            FileUtil.del(filePath);
        }
    }

    /**
     * 校验文件是否可下载
     * @param fileName
     * @return
     */
    public static boolean checkAllowDownload(String fileName) {
        // 禁止目录上跳级别
        if (StrUtil.contains(fileName, "..")) {
            return false;
        }
        return true;
    }

    /**
     * 校验上传的文件
     * @param file  上传的文件
     */
    public static void checkUploadFile(MultipartFile file) {
        if (file == null) {
            throw new BusinessException("文件内容不能为空");
        }
        int maxUploadFileSize =  FileProperties.getMaxUploadFileSize() * 1024*1024;
        // 文件大小校验
        if (file.getSize() >maxUploadFileSize) {
            throw new BusinessException("文件大小不能超过" + (maxUploadFileSize / 1024 / 1204) + "MB");
        }
    }

    /**
     * 初始化（目标）文件
     * @param fileName  文件名称（包含路径）
     * @return
     */
    public static File initFile(String fileName) {
        File destFile =new File(fileName);
        FileUtil.mkParentDirs(destFile);
        // 获取服务器上真实文件地址（避免获取到tomcat根目录下的路径）
        File realFile = new File(destFile.getAbsolutePath());
        return realFile;
    }


    /**
     * 设置文件下载响应头
     * @param request
     * @param fileName
     * @return
     */
    public String setFileDownloadHeader(HttpServletRequest request, String fileName) {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        try {
            if (agent.contains("MSIE"))
            {
                // IE浏览器
                filename = URLEncoder.encode(filename, "utf-8");
                filename = filename.replace("+", " ");
            }
            else if (agent.contains("Firefox"))
            {
                // 火狐浏览器
                filename = new String(fileName.getBytes(), "ISO8859-1");
            }
            else if (agent.contains("Chrome"))
            {
                // google浏览器
                filename = URLEncoder.encode(filename, "utf-8");
            }
            else
            {
                // 其它浏览器
                filename = URLEncoder.encode(filename, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            logger.info("文件名编码失败。", e.getMessage());
        }
        return filename;
    }


    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os)
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw new BusinessException("文件写入输出流失败。", e);
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }
}
