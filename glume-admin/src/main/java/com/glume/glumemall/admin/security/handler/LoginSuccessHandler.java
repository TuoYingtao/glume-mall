package com.glume.glumemall.admin.security.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glume.common.core.constant.HttpStatus;
import com.glume.common.core.utils.JwtUtils;
import com.glume.common.core.utils.R;
import com.glume.common.core.utils.ServletUtils;
import com.glume.common.core.utils.StringUtils;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.security.AccountUser;
import com.glume.glumemall.admin.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.core.BoundHashOperations;
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
import java.util.Map;

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
        // 创建用户令牌
        String token = createToken(authentication);
        // 设置在线用户信息
        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setUsername(authentication.getName());
        loginEntity.setToken(token);
        WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) authentication.getDetails();
        loginEntity.setIp(webAuthenticationDetails.getRemoteAddress());
        loginEntity.setCreateTime(new Date());
        UserAgent parse = UserAgentUtil.parse(httpServletRequest.getHeader("User-Agent"));
        loginEntity.setBrowser(parse.getBrowser().getName());
        loginEntity.setEngine(parse.getEngine().getName());
        loginEntity.setEngineVersion(parse.getEngineVersion());
        loginEntity.setVersion(parse.getVersion());
        loginEntity.setOs(parse.getOs().getName());
        loginEntity.setPlatform(parse.getPlatform().toString());

        BoundHashOperations hashOps = redisTemplate.boundHashOps(USER_ON_LIEN);
        LoginEntity loginServiceOne = loginService.getOne(new QueryWrapper<LoginEntity>().eq("username", authentication.getName()));
        // 判断当前登录用户是否已在登录列表
        if (StringUtils.isNotNull(loginServiceOne)) {
            // 存在，更新用户令牌
            loginService.update(loginEntity,new QueryWrapper<LoginEntity>().eq("username",authentication.getName()));
            // 删除旧缓存
            if (hashOps.hasKey(authentication.getName())) {
                hashOps.delete(authentication.getName());
            }
        } else {
            // 不存在，加入在线列表
            loginService.save(loginEntity);
        }
        // 将信息存入缓存中
        hashOps.put(authentication.getName(),JSON.toJSONString(loginEntity));
        R result = R.ok()
                .put("data",new HashMap<String,Object>(){{put("token",token);}})
                .put("code", HttpStatus.SUCCESS);
        ServletUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }

    /**
     * 生成 JWT
     */
    private String createToken(Authentication authentication) {
        AccountUser accountUser = (AccountUser) authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        BeanMap beanMap = BeanMap.create(accountUser);
        for (Object key : beanMap.keySet()) {
            map.put(key.toString(),beanMap.get(key));
        }
        map.entrySet().removeIf(item -> item.getKey() == AccountUser.UserFields.PASSWORD.getMessage()
                || item.getKey() == AccountUser.UserFields.AUTHORITIES.getMessage()
                || item.getKey() == AccountUser.UserFields.ACCOUNT_NON_EXPIRED.getMessage()
                || item.getKey() == AccountUser.UserFields.ACCOUNT_NON_LOCKED.getMessage()
                || item.getKey() == AccountUser.UserFields.CREDENTIALS_NON_EXPIRED.getMessage()
                || item.getKey() == AccountUser.UserFields.ENABLED.getMessage());
        Claims claims = Jwts.claims(map);
        String token = jwtUtils.generateToken(accountUser.getUsername(),claims);
        return token;
    }
}
