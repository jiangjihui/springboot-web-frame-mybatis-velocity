package com.jjh.framework.swagger;

import cn.hutool.core.collection.CollectionUtil;
import com.jjh.framework.jwt.JWTConstants;
import com.jjh.framework.properties.ApplicationInfoProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
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
import java.util.function.Predicate;

/**
 * Swagger2API文档的配置
 *
 * @author jjh
 * @date 2019/10/30
 **/
@Configuration
@EnableAutoConfiguration
@ConditionalOnBean(SwagggerProperties.class)
@EnableConfigurationProperties(SwagggerProperties.class)
@EnableSwagger2WebMvc
public class SwaggerConfig implements ApplicationContextAware {

    /** 用户配置 */
    private final SwagggerProperties properties;

    private ConfigurableApplicationContext configurableApplicationContext;

    public SwaggerConfig(SwagggerProperties swagggerProperties) {
        this.properties = swagggerProperties;
    }

    /**
     * 创建swagger接口分组
     * @return
     */
    @Bean
    public String createDocket() {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(Docket.class);

        beanDefinitionBuilder.addConstructorArgValue(DocumentationType.SWAGGER_2);

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();

        // 注册所有定义的接口分组
        if (CollectionUtil.isNotEmpty(properties.getGroups())) {
            properties.getGroups().forEach(group -> {
                beanFactory.registerBeanDefinition(group.getGroupName(), beanDefinition);
                Docket docket = configurableApplicationContext.getBean(group.getGroupName(), Docket.class);
                // 获取需要扫描的接口目录
                Predicate<RequestHandler> apiPackage = null;
                for (String str : group.getPackages()) {
                    if (apiPackage == null){
                        apiPackage = RequestHandlerSelectors.basePackage(str);
                    }
                    else {
                        apiPackage.or(RequestHandlerSelectors.basePackage(str));
                    }
                }
                docket
                        // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                        .apiInfo(apiInfo())
                        // 设置分组名称
                        .groupName(group.getGroupName())
                        // 设置哪些接口暴露给Swagger展示
                        .select()
                        .apis(apiPackage)
                        .paths(PathSelectors.any())
                        .build()
                        .securitySchemes(Collections.singletonList(securityScheme()));
            });
        }

        return "createDocket";
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
    }
}
