package com.glume.glumemall.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
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
