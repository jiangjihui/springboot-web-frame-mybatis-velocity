package com.jjh.framework.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口信息
 *
 * @author jjh
 * @date 2021/03/11
 **/
@Data
@Component
@ConfigurationProperties(prefix = "app.swagger")
public class SwagggerProperties {

    /** 接口分组列表 */
    private List<SwaggerGroup> groups = new ArrayList<>();

    /** 标题*/
    private static String title;
    /** 描述*/
    private static String description;
    /** 版本*/
    private static String version;


}
