package com.jjh.framework.config;

import com.jjh.framework.properties.FileProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .allowedOrigins("*")
//                .allowedMethods("*");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 资源文件访问（路径）
        String resource = "/"+ FileProperties.getStaticUrl()+"**";
        registry.addResourceHandler(resource).addResourceLocations("file:" + FileProperties.getStaticPath()+ File.separator);

        /** swagger配置 */
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
