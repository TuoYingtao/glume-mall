package com.glume.common.core.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * JWT 工具类
 * @author tuoyingtao
 * @create 2021-10-18 11:03
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtUtils {

    /* JWT加解密使用的密钥 */
    private String secret;

    /* JWT的超期限时间 */
    private Long expiration;

    /* JWT 名称 */
    private String header;

    /**
     * 创建Token
     * @param username
     * @return
     */
    public String generateToken(String username) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512,secret);
        return jwtBuilder.compact();

    }

    /**
     * 从Token中获取用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        return getTokenBody(token).getSubject();
    }

    private Claims getTokenBody(String token) {
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
     * @param token 用户请求中的令牌
     * @return
     */
    public boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 验证 Token 是否失效
     * @param currentToken 用户请求中的令牌
     * @param cacheToken 缓存中存贮的令牌
     * @return 验证 Token 唯一ID（jit）false-同一个凭证 true-不是同一个凭证
     */
    public boolean isExpiration(String currentToken, String cacheToken) {
        Claims currentBody = getTokenBody(currentToken);
        Claims cacheBody = getTokenBody(cacheToken);
        String currentTokenID = currentBody.get(Claims.ID).toString();
        String cacheTokenID = cacheBody.get(Claims.ID).toString();
        if (!currentTokenID.equals(cacheTokenID)) {
            return true;
        }
        return false;
    }
}
