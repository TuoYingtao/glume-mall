package com.glume.glumemall.admin.security;

import com.alibaba.fastjson.JSON;
import com.glume.glumemall.admin.util.JwtUtils;
import com.glume.glumemall.common.constant.HttpStatus;
import com.glume.glumemall.common.utils.R;
import com.glume.glumemall.common.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * security 登录成功结果处理
 * @author tuoyingtao
 * @create 2021-10-18 17:02
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        //生成 JWT,
        String token = jwtUtils.generateToken(authentication.getName());
        R result = R.ok()
                .put("data",new HashMap<String,Object>(){{put("token",token);}})
                .put("code", HttpStatus.SUCCESS);
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
