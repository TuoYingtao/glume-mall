package com.glume.glumemall.common.config;

import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tuoyingtao
 * @create 2021-10-15 11:38
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /** 是否开启swagger */
    @Value("${swagger.enabled}")
    boolean swaggerEnable;

    @Value("${jwt.header}")
    String headerToken;

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                // 是否启用Swagger
                .enable(swaggerEnable)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(webApiInfo())
                // 设置请求的统一前缀
                .pathMapping("/")
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                // 扫描指定包中的swagger注解
                // .apis(RequestHandlerSelectors.basePackage("com.glume.glumemall.admin.controller"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .globalOperationParameters(getParameterList());
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes()
    {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey(headerToken, headerToken, In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts()
    {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .build());
        return securityContexts;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /**
     * Header参数配置
     * @return
     */
    private List<Parameter> getParameterList() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> arrayList = new ArrayList<>();
        parameterBuilder.name(headerToken).description("Token令牌").modelRef(new ModelRef("String"))
                .parameterType("header").required(false).build();
        arrayList.add(parameterBuilder.build());
        return arrayList;
    }

    /**
     * 添加摘要信息
     * @return
     */
    public ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                // 设置标题
                .title("标题：glume-mall API 文档")
                // 描述
                .description("描述：glume-mall 接口文档")
                .termsOfServiceUrl("https://github.com/TuoYingtao/glume-mall")
                // 版本
                .version("版本号：1.0.0")
                // 许可证
                .license("许可证：The Apache License")
                // 许可证地址
                .licenseUrl("https://github.com/TuoYingtao/glume-mall/blob/main/LICENSE")
                .build();
    }
}
