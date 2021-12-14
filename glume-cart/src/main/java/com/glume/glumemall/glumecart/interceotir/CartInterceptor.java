package com.glume.glumemall.glumecart.interceotir;

import com.glume.common.core.constant.AuthServerConstant;
import com.glume.common.core.constant.CartConstant;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.glumecart.to.UserInfoTo;
import com.glume.common.core.to.MemberRespTo;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @author tuoyingtao
 * @create 2021-12-13 10:37
 */
public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> toThreadLocal = new ThreadLocal<>();

    /**
     * 业务执行之前
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
        MemberRespTo data = (MemberRespTo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (StringUtils.isNotNull(data)) {
            userInfoTo.setUserId(data.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (StringUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_KEY)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setIsTempUser(true);
                }
            }
        }
        // 如果没有临时用户一定分配一个临时用户
        if (StringUtils.isNull(userInfoTo.getUserKey())) {
            userInfoTo.setUserKey(UUID.randomUUID().toString());
        }
        toThreadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 业务执行之后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = toThreadLocal.get();
        if (!userInfoTo.getIsTempUser()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_KEY, userInfoTo.getUserKey());
            cookie.setDomain("glumemall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
