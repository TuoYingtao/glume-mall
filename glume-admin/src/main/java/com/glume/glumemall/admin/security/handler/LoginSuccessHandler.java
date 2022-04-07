package com.glume.glumemall.admin.security.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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

    @Autowired
    LoginService loginService;

    @Autowired
    RedisTemplate redisTemplate;

    public static final String USER_ON_LIEN = "admin:user:";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        //生成 JWT,
        String token = jwtUtils.generateToken(authentication.getName());
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setUsername(authentication.getName());
        loginEntity.setToken(token);
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        loginEntity.setIp(webAuthenticationDetails.getRemoteAddress());
        loginEntity.setCreateTime(new Date());
        BoundHashOperations hashOps = redisTemplate.boundHashOps(USER_ON_LIEN);
        LoginEntity loginServiceOne = loginService.getOne(new QueryWrapper<LoginEntity>().eq("username", authentication.getName()));
        if (StringUtils.isNotNull(loginServiceOne)) {
            QueryWrapper<LoginEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("username",authentication.getName());
            loginService.update(loginEntity,wrapper);
            if (hashOps.hasKey(authentication.getName())) {
                hashOps.delete(authentication.getName());
            }
        } else {
            loginService.save(loginEntity);
        }
        hashOps.put(authentication.getName(),JSON.toJSONString(loginEntity));
        R result = R.ok()
                .put("data",new HashMap<String,Object>(){{put("token",token);}})
                .put("code", HttpStatus.SUCCESS);
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
