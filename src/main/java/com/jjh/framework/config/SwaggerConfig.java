package com.jjh.framework.config;

import com.jjh.framework.jwt.JWTConstants;
import com.jjh.framework.properties.ApplicationInfoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.Collections;

/**
 * Swagger2API文档的配置
 *
 * @author jjh
 * @date 2019/10/30
 **/
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    /**
     * 系统管理相关接口
     * @return
     */
    @Bean
    public Docket createSystemApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置分组名称
                .groupName("system")
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(
                        RequestHandlerSelectors.basePackage("com.jjh.business.system")
                        .or(RequestHandlerSelectors.basePackage("com.jjh.business.common"))
                // 扫描指定包中的swagger注解
                //.apis(RequestHandlerSelectors.basePackage("com.ruoyi.project.tool.swagger"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                ).paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }

    /**
     * 业务相关接口
     * @return
     */
    @Bean
    public Docket createBusinessApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置分组名称
                .groupName("business")
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(
                        RequestHandlerSelectors.basePackage("com.jjh.business.demo")
                // 扫描指定包中的swagger注解
                //.apis(RequestHandlerSelectors.basePackage("com.ruoyi.project.tool.swagger"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                ).paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()));
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(ApplicationInfoProperties.getApiTitle())
                .description(ApplicationInfoProperties.getDescription())
                .contact(new Contact(ApplicationInfoProperties.getAuthor(), null, ApplicationInfoProperties.getContact()))
                .version(ApplicationInfoProperties.getVersion())
                .build();
    }


    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
     * @return
     */
    @Bean
    SecurityScheme securityScheme() {
        return new ApiKey(JWTConstants.X_ACCESS_TOKEN, JWTConstants.X_ACCESS_TOKEN, "header");
    }

}
