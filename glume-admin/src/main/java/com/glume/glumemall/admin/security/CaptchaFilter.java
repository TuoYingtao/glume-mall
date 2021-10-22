package com.glume.glumemall.admin.security;

import com.glume.glumemall.admin.exception.CaptchaException;
import com.glume.glumemall.admin.util.RedisUtils;
import com.glume.glumemall.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码图片 验证处理（在验证用户名、密码之前处理）
 * @author tuoyingtao
 * @create 2021-10-18 17:11
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = httpServletRequest.getRequestURI();
        if ("/admin/user/login".equals(requestURI) && "POST".equals(httpServletRequest.getMethod())) {
            // 校验验证码
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 如果不正确，就跳转到认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    /**
     * 校验验证码
     * @param httpServletRequest
     */
    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("key");
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(key)) {
            throw new CaptchaException("key值与验证码不能为空！");
        }
        if (!code.equals(redisUtils.hget(RedisConstant.CAPTCHA_KEY,key))) {
            throw new CaptchaException("验证码错误");
        }
        redisUtils.hdel(RedisConstant.CAPTCHA_KEY,key);
    }
}
