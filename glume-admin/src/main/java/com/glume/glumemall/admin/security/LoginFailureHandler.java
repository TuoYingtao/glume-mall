package com.glume.glumemall.admin.security;

import cn.hutool.json.JSONUtil;
import com.glume.glumemall.common.utils.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * security 登录失败异常处理
 * @author tuoyingtao
 * @create 2021-10-18 16:53
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        R result = R.error()
                .put("code",HttpServletResponse.SC_NOT_IMPLEMENTED)
                .put("msg", e.getMessage());
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
