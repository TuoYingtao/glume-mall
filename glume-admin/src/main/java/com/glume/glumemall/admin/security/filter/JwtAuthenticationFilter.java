package com.glume.glumemall.admin.security.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.glume.common.core.utils.JwtUtils;
import com.glume.glumemall.admin.entity.LoginEntity;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.security.UserDetailsServiceImpl;
import com.glume.glumemall.admin.security.handler.LoginSuccessHandler;
import com.glume.glumemall.admin.security.handler.LogoutSuccessHandlerImpl;
import com.glume.glumemall.admin.service.BlackListService;
import com.glume.glumemall.admin.service.impl.UserServiceImpl;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.jar.JarException;

/**
 * Jwt 认证过滤
 * @author tuoyingtao
 * @create 2021-10-19 10:17
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Resource
    private JwtUtils jwtUtils;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BlackListService blackListService;

    @Autowired
    LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private String username = null;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader(jwtUtils.getHeader());
        // token 为空，白名单跳过验证
        if (StrUtil.isBlankOrUndefined(token)) {
            chain.doFilter(request,response);
            return;
        }
        try {
            username = jwtUtils.getUserNameFromToken(token);
        } catch (Exception e) {
            throw new JwtException("token 异常");
        }
        jwtBlackList(token);
        jwtExpiation(token);

        UserEntity userEntity = userService.getByUserDetail(username);
        // 设置权限主体
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(username,null,userDetailsService.getUserAuthority(userEntity.getUserId()));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    /**
     * 验证 JWT 是否存在黑名单当中
     */
    private void jwtBlackList(String token) throws JarException {
        try {
            blackListService.userTokenVerify(token);
        } catch (Exception e) {
            logoutSuccessHandler.userOnLienHandler(username);
            LOGGER.error("{}--当前用户已被踢出下线！", username);
            throw new JwtException("当前用户已被踢出下线！");
        }
    }

    /**
     * 验证 JWT 唯一ID是否相等,且 Token 是否过期
     * @param token
     * @param userNameFromToken
     */
    private void jwtExpiation(String token) {
        if (jwtUtils.isExpiration(token)) {
            logoutSuccessHandler.userOnLienHandler(username);
            LOGGER.error("{}--token已过期", username);
            throw new JwtException("token已过期");
        }
        BoundHashOperations hashOps = redisTemplate.boundHashOps(LoginSuccessHandler.USER_ON_LIEN);
        String result = hashOps.get(username).toString();
        LoginEntity loginEntity = JSON.parseObject(result, LoginEntity.class);
        if (jwtUtils.isExpiration(token, loginEntity.getToken())) {
            LOGGER.error("{}--当前用户已在其它地方登录！", username);
            throw new JwtException("当前用户已在其它地方登录！");
        }
    }
}
