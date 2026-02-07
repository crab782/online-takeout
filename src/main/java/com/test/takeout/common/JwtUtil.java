package com.test.takeout.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtil {

    /**
     * 密钥
     */
    private static final String SECRET_KEY = "your-secret-key";

    /**
     * 过期时间（毫秒）
     */
    private static final long EXPIRE_TIME = 86400000L; // 24小时

    /**
     * 生成token
     * @param userId 用户ID
     * @return token
     */
    public static String generateToken(Long userId) {
        // 创建claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 创建token
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析token
     * @param token token
     * @return claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从token中获取用户ID
     * @param token token
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 验证token是否有效
     * @param token token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
