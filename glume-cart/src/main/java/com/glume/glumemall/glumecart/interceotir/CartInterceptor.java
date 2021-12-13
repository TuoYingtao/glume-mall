package com.glume.glumemall.glumecart.interceotir;

import com.glume.common.core.constant.CartConstant;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.glumecart.to.UserInfoTo;
import com.glume.glumemall.glumecart.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:37
 */
@Component
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> toThreadLocal = new ThreadLocal<>();

    /**
     * 目标方法执行之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberRespVo data = (MemberRespVo) session.getAttribute("data");
        if (StringUtils.isNotNull(data)) {
            userInfoTo.setUserId(data.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (StringUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_KEY)) {
                    userInfoTo.setUserKey(cookie.getValue());
                }
            }
        }
        toThreadLocal.set(userInfoTo);
        return true;
    }
}
