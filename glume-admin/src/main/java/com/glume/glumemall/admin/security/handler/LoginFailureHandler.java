package com.glume.glumemall.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * security 登录失败异常处理
 * @author tuoyingtao
 * @create 2021-10-18 16:53
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) {
        R result = R.error()
                .put("code", HttpStatus.ERROR)
                .put("msg", e.getMessage());
        ServletUtils.renderString(httpServletResponse, HttpStatus.ERROR, JSON.toJSONString(result));
    }
}
