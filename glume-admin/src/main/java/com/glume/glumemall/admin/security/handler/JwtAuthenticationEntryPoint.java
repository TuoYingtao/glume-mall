package com.glume.glumemall.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证失败异常处理
 * @author tuoyingtao
 * @create 2021-10-19 10:39
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        R result = R.error(HttpStatus.UNAUTHORIZED,"认证失败请先登录!");
        String context = JSON.toJSONString(result);
        ServletUtils.renderString(httpServletResponse,HttpStatus.UNAUTHORIZED,context);
    }
}
