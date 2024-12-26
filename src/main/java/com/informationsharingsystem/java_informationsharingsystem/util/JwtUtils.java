package com.informationsharingsystem.java_informationsharingsystem.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    // 定义密钥和令牌过期时间
    private static final String SECRET_KEY = "123456"; // 替换为你的密钥
    private static final long EXPIRATION_TIME = 86400000; // 令牌有效期 (1 天，单位：毫秒)

    /**
     * 生成 JWT 令牌
     *
     * @param username 用户名
     * @return 生成的 JWT 令牌
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // 用户信息
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 使用密钥进行签名
                .compact();
    }

    /**
     * 验证 JWT 令牌
     *
     * @param token JWT 令牌
     * @return 如果令牌有效返回 true，否则返回 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token); // 验证令牌
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Invalid signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument: " + e.getMessage());
        }
        return false;
    }

    /**
     * 从 JWT 令牌中提取用户名
     *
     * @param token JWT 令牌
     * @return 提取到的用户名
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 从主体中获取用户名
    }
}