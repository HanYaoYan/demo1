package com.example.demo.util;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

/**
 * 这是一个生产JWT令牌的工具类, 包含功能
 * 1. generateToken() 接受主题并产生返回一个JWT令牌(字符串)
 * 2. extractClaims() 用于接受令牌并返回一个Claims类(该类可用于解析JWT)
 * 3. extractUsername() 接受JWT令牌并提取他的用户名
 * 4. isTokenExpired() 接受JWT令牌并验证是否失效
 * 5. validateToken() 接受JWT令牌并验证是否有效
 */
@Component
public class JWTUtil {
    private static final String SECRET_KEY = "sfsfsdffvsdvewwefqefafasasdadsadadqawcascasdasdascascascascadwdafawdasscasascasascasdqw"; // 使用一个256位的密钥
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1小时的过期时间

    // 生成JWT令牌
    public String generateToken(String username) {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), SignatureAlgorithm.HS256.getJcaName());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    // 从JWT令牌中提取Claims信息
    private Claims extractClaims(String token) {
        SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 提取JWT中的用户名
    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    // 检查JWT是否过期
    private boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }

    // 验证JWT令牌
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
}