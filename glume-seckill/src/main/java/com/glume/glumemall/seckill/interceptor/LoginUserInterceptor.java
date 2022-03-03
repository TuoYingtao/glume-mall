package com.glume.glumemall.seckill.interceptor;

import com.glume.common.core.constant.AuthServerConstant;
import com.glume.common.core.to.MemberRespTo;
import com.glume.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * @author tuoyingtao
 * @create 2022-03-03 16:08
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static final ThreadLocal<MemberRespTo> toThreadLocal = new ThreadLocal<>();

    /**
     * 前置拦截 判断用户是否登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 请求放过
        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/kill", uri);
        if (match) {
            // 请求拦截
            MemberRespTo attribute = (MemberRespTo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
            if (StringUtils.isNotNull(attribute)) {
                toThreadLocal.set(attribute);
                return true;
            } else {
                request.getSession().setAttribute("msg","请先进行登录");
                response.sendRedirect("http://auth.glumemall.com/login.html");
                return false;
            }
        }
        return true;
    }
}
