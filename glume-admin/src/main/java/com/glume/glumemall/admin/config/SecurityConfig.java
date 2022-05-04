package com.glume.glumemall.admin.config;

import com.glume.glumemall.admin.security.handler.*;
import com.glume.glumemall.admin.security.*;
import com.glume.glumemall.admin.security.filter.CaptchaFilter;
import com.glume.glumemall.admin.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

    /** 退出登录处理 */
    @Autowired
    LogoutSuccessHandlerImpl logoutSuccessHandler;

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
        "/admin/user/logout",
        "/admin/user/captcha",
        "/admin/user/test/password",
    };

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.cors();

        /** CSRF禁用，因为不使用session */
        httpSecurity.csrf().disable();

        /** 基于token, 所以不需要session */
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /** 认证失败处理类 */
        httpSecurity.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        /** 登录配置 定义登录接口（默认：/login） */
        httpSecurity.formLogin().loginProcessingUrl("/admin/user/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler);

        /** 退出登录配置 定义退出登录接口（默认：/logout） */
        httpSecurity.logout().logoutUrl("/admin/user/logout")
                .logoutSuccessHandler(logoutSuccessHandler);

        /** 拦截规则 白名单跳过 其它的会自动拦截 */
        httpSecurity.authorizeRequests()
                .antMatchers(URL_WHITELIST).anonymous()
                .antMatchers(
                        "/",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/favicon.ico").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();

        /** 自定义过滤器 */
        httpSecurity.addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class) // 在什么过滤器之前
            .addFilter(jwtAuthenticationFilter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    /** 强散列哈希加密实现 */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        // 把默认值改为 false 解决无法捕获UsernameNotFoundException 问题
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }
}
