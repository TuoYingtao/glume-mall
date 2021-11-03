package com.glume.common.core.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 客户端工具类
 * @author tuoyingtao
 * @create 2021-10-23 11:59
 */
public class ServletUtils {

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /** 获取 request */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /** 获取 response */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /** 获取 session */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 将字符串渲染到客户端
     * @param response 渲染对象
     * @param string 待渲染的字符串
     * @return
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String renderString(HttpServletResponse response,Integer code, String string) {
        try {
            response.setStatus(code);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
