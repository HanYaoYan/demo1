package com.example.demo.util;
/**
 * 添加报错处理
 *
 */

import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class AuthorizingUtil {



    /**
     * 从Cookies里面得到此时用户的JWT
     */
    private static String getJwtFromCookies(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null; // 如果没有找到名为 "jwt" 的 Cookie，则返回 null
    }

    public static String getJwtUsername(HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt == null) {
            return null; // 或者抛出异常
        }
        JWTUtil jwtUtil = new JWTUtil();
        return jwtUtil.extractUsername(jwt);
    }

    public static boolean judgeUser(String user, HttpServletRequest request) {
        String jwt = getJwtFromCookies(request);
        if (jwt == null) {
            return false; // 或者抛出异常
        }
        JWTUtil jwtUtil = new JWTUtil();
        return jwtUtil.validateToken(jwt, user);
    }
}
