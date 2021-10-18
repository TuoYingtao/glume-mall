package com.glume.glumemall.admin.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author tuoyingtao
 * @create 2021-10-18 11:03
 */
@Component
public class JwtUtils {

    /* JWT加解密使用的密钥 */
    @Value("${jwt.secret}")
    private String secret;

    /* JWT的超期限时间(60*60*24*7) */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 创建Token
     * @param username
     * @return
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,secret)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    /**
     * 从Token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        return getTokenBody(token).getSubject();
    }

    public Claims getTokenBody(String token) {
        Claims body = null;
        try {
            body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    /**
     * 验证Token是否过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }
}
