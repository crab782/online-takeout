package com.test.takeout;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // 测试原始密码
        String rawPassword = "123456";
        
        // 数据库中的加密密码
        String dbPassword = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        // 验证密码
        boolean matches = passwordEncoder.matches(rawPassword, dbPassword);
        
        System.out.println("原始密码: " + rawPassword);
        System.out.println("数据库密码: " + dbPassword);
        System.out.println("密码匹配结果: " + matches);
        
        // 生成新的加密密码
        String newEncodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("新生成的加密密码: " + newEncodedPassword);
        
        // 验证新生成的密码
        boolean newMatches = passwordEncoder.matches(rawPassword, newEncodedPassword);
        System.out.println("新密码匹配结果: " + newMatches);
    }
}
