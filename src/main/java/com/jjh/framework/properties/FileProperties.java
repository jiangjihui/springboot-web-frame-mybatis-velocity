package com.jjh.framework.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 文件配置
 *
 * @author jjh
 * @date 2019/11/19
 **/
@Component
@ConfigurationProperties(prefix = "app.file")
public class FileProperties {

    /** 基础文件夹路径*/
    private static String basePath;

    /** 资源访问Url*/
    private static String staticUrl;

    /** 静态资源保存路径*/
    private static String staticDir;

    /** 资源目录*/
    private static String resourceDir;

    /** 上传大小限制*/
    private static Integer maxUploadFileSize;


    public static String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        FileProperties.basePath = basePath;
    }

    public static String getResourceDir() {
        return resourceDir;
    }

    public void setResourceDir(String resourceDir) {
        FileProperties.resourceDir = resourceDir;
    }

    public static Integer getMaxUploadFileSize() {
        return maxUploadFileSize;
    }

    public void setMaxUploadFileSize(Integer maxUploadFileSize) {
        FileProperties.maxUploadFileSize = maxUploadFileSize;
    }

    public static String getStaticUrl() {
        return staticUrl;
    }

    public void setStaticUrl(String staticUrl) {
        FileProperties.staticUrl = staticUrl;
    }

    public static String getStaticDir() {
        return staticDir;
    }

    public void setStaticDir(String staticDir) {
        FileProperties.staticDir = staticDir;
    }

    public void setStaticPath(String staticPath) {
        FileProperties.staticDir = staticPath;
    }

    /**
     * 获取资源文件路径
     * @return  文件路径
     */
    public static String getResourcePath() {
        return FileProperties.getBasePath() + File.separator + FileProperties.getResourceDir();
    }

    /**
     * 获取静态资源文件路径
     * @return  文件路径
     */
    public static String getStaticPath() {
        return FileProperties.getBasePath() + File.separator + FileProperties.getStaticDir();
    }
}
