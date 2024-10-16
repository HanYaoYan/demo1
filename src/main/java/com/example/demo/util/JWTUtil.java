package com.example.demo.util;
/**
 * 这是一个生产Jwts令牌的工具类, 包含功能
 * 1. generatetoken() 接受主题并产生返回一个jwts令牌(字符串)
 * 2. claims() 用于接受令牌并返回一个claims类(该类可用于解析jwts类)
 * 3. extractUsername() 接受jwt令牌并提取他的用户名
 * 4. isTokenExpired() 接受jwts令牌并验证是否失效
 * 5. validateToken() 接受jwts令牌并验证是否不可用
 */
//以下包我已经在java项目依赖中用maven进行导入,并没有在pom中进行显式依赖

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
//
@Component
public class JWTUtil {
    private static final int KEY_SIZE = 256; // 使用复杂的随机密钥,此处需要调用另一个产生随机数的密钥函数
    private  String SECRET_KEY;

    public JWTUtil(){
        SECRET_KEY=generateSecretKey();
    }
    /**
     * 这是一个Secret_Key生成类,用安全随机数生成了256位的secret_Key
     * @return
     */
    public static String generateSecretKey(){
        SecureRandom secureRandom=new SecureRandom();
        byte[] key = new byte[KEY_SIZE/8];
        secureRandom.nextBytes(key);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(key);
    }
    /**
     *该函数用于接收导入的用户数据,为其生成一个JWT令牌
     * @param username 在控制器中接收主题
     * @return 返回jwt令牌
     */
    public String generateToken(String username) {
        long expirationTime = 1000 * 60 * 60; // 设置有效时间为1小时
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTime);

       Jwts.builder().setSubject(username);
       Jwts.builder().setIssuedAt(now);
       Jwts.builder().setExpiration(expirationDate);
       Jwts.builder().signWith(SignatureAlgorithm.HS512, SECRET_KEY);
       String jwt=Jwts.builder().compact();
        return jwt;
    }

    /**
     * 该函数用于从一个给定的jwt令牌中提取claims信息
     * @param token 接收令牌
     * @return 返回一个Claims类,里面包含了Jwts的所有声明信息
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()//创建一个解析器
                .setSigningKey(SECRET_KEY)//验证密钥
                .parseClaimsJws(token)//验证签名并提取有效负载部分,成功后会返回Jwts<Claims>对象
                .getBody();
    }

    /**
     * 调用Claim供给,取令牌中的主题名字
     * @param token 传入一个Jwts令牌
     * @return 返回一个用户名字符串
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * 提取Jwts的失效时间,进行验证,其中extractClaims(token).getExpiration()返回了一个Date类
     * @param token 传入应该Jwts令牌
     * @return 返回一个布尔值,代表是否失效
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * 该函数用来验证该令牌是否仍然有效
     * @param token 传入的令牌
     * @param username 用户名
     * @return 一个布尔值,表示是否有效
     */
    public boolean validateToken(String token, String username) {
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }
}
