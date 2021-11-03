package com.glume.glumemall.admin.security.filter;

import cn.hutool.core.util.StrUtil;
import com.glume.glumemall.admin.entity.UserEntity;
import com.glume.glumemall.admin.security.UserDetailsServiceImpl;
import com.glume.glumemall.admin.service.impl.UserServiceImpl;
import com.glume.glumemall.common.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt 认证过滤
 * @author tuoyingtao
 * @create 2021-10-19 10:17
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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
        String userNameFromToken = null;
        try {
            userNameFromToken = jwtUtils.getUserNameFromToken(token);
        } catch (Exception e) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isExpiration(token)) {
            throw new JwtException("token已过期");
        }
        String username = jwtUtils.getUserNameFromToken(token);
        UserEntity userEntity = userService.getByUserDetail(username);
        // 设置权限主体
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userNameFromToken,null,userDetailsService.getUserAuthority(userEntity.getUserId()));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }
}
