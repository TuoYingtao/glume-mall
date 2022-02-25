package com.glume.glumemall.member.interceptor;

import com.glume.common.core.constant.AuthServerConstant;
import com.glume.common.core.to.MemberRespTo;
import com.glume.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会员拦截器
 * @author tuoyingtao
 * @create 2021-12-16 9:52
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespTo> toThreadLocal = new ThreadLocal<>();

    /**
     * 前置拦截 判断用户是否登录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
}
