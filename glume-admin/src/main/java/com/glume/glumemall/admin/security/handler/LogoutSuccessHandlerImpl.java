package com.glume.glumemall.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * TODO 退出登录
 * @author tuoyingtao
 * @create 2022-03-10 15:27
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    LoginService loginService;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader(jwtUtils.getHeader());
        String useranme = jwtUtils.getUserNameFromToken(token);
        userOnLienHandler(useranme);
        R result = R.ok("退出成功！");
        ServletUtils.renderString(response,JSON.toJSONString(result));
    }

    public void userOnLienHandler(String username) {
        BoundHashOperations hashOps = redisTemplate.boundHashOps(LoginSuccessHandler.USER_ON_LIEN);
        if (hashOps.hasKey(username)) {
            hashOps.delete(username);
        }
        loginService.remove(new QueryWrapper<LoginEntity>().eq("username",username));
    }
}
