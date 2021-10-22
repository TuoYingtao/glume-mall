package com.glume.glumemall.admin.config;

import com.glume.glumemall.admin.exception.JwtAccessDeniedHandler;
import com.glume.glumemall.admin.exception.JwtAuthenticationEntryPoint;
import com.glume.glumemall.admin.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 配置
 * @author tuoyingtao
 * @create 2021-10-18 9:59
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /** security 登录成功结果处理 */
    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    /** 登录失败异常配置 */
    @Autowired
    LoginFailureHandler loginFailureHandler;

    /** 自定义的过滤器 */
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    /** 用户名密码验证，获取用户信息 */
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /** 定义白名单 */
    private static final String[] URL_WHITELIST = {
        "/admin/user/login",
        "/logout",
        "/admin/user/captcha",
        "/admin/user/test/password",
        "/swagger-ui.html",
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/webjars/springfox-swagger-ui/**",
        "/favicon.ico"
    };

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            /** 登录配置 */
            .formLogin()
                .loginProcessingUrl("/admin/user/login") //定义登录接口（默认：/login）
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
        .and()
            /** 禁用session */
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            /** 拦截规则 */
            .authorizeRequests()
                // 白名单跳过
                .antMatchers(URL_WHITELIST).permitAll()
                // 其它的会自动拦截
                .anyRequest().authenticated()
        .and()
            /** 异常处理器 */
            .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
        .and()
            /** 自定义过滤器 */
            .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class) // 在什么过滤器之前
            .addFilter(jwtAuthenticationFilter())
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
