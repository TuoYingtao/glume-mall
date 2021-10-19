package com.glume.glumemall.admin.security;

import cn.hutool.core.util.StrUtil;
import com.glume.glumemall.admin.util.JwtUtils;
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
        String userNameFromToken = jwtUtils.getUserNameFromToken(token);
        if (userNameFromToken == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isExpiration(token)) {
            throw new JwtException("token已过期");
        }
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userNameFromToken,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }
}
