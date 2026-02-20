package com.test.takeout.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
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
    private static final String SECRET_KEY = "your-secret-key-here-should-be-longer-for-security";

    /**
     * 过期时间（毫秒）
     */
    private static final long EXPIRE_TIME = 86400000L; // 24小时

    /**
     * 获取密钥
     * @return 密钥
     */
    private static Key getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

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
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .setIssuedAt(new Date())
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token
     * @param token token
     * @return claims
     */
    public static Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Token解析失败: " + e.getMessage());
        }
    }

    /**
     * 从token中获取用户ID
     * @param token token
     * @return 用户ID
     */
    public static Long getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return Long.valueOf(claims.get("userId").toString());
        } catch (Exception e) {
            throw new RuntimeException("获取用户ID失败: " + e.getMessage());
        }
    }

    /**
     * 验证token是否有效
     * @param token token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断token是否过期
     * @param token token
     * @return 是否过期
     */
    private static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取token的过期时间
     * @param token token
     * @return 过期时间
     */
    public static Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }
}
