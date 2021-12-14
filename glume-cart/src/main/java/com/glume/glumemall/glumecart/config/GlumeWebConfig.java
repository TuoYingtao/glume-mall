package com.glume.glumemall.glumecart.config;

import com.glume.glumemall.glumecart.interceotir.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author tuoyingtao
 * @create 2021-12-14 10:02
 */
@Configuration
public class GlumeWebConfig implements WebMvcConfigurer {

    /** 添加拦截器 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
}
