package com.glume.glumemall.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
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

    @Value("${swagger.enabled}")
    boolean swaggerEnable;

    @Value("${jwt.header}")
    String headerToken;

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(webApiInfo())
                .enable(swaggerEnable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.glume.glumemall.admin.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getParameterList());
    }

    public ApiInfo webApiInfo() {
        return new ApiInfoBuilder().title("glume-mall API 文档")
                .description("glume-mall 接口文档")
                .termsOfServiceUrl("https://github.com/TuoYingtao/glume-mall")
                .version("1.0.0")
                .license("The Apache License")
                .licenseUrl("https://github.com/TuoYingtao/glume-mall/blob/main/LICENSE")
                .build();
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
}
