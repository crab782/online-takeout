package com.test.takeout;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 测试加密
        String rawPassword = "123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("加密后的密码: " + encodedPassword);
        
        // 测试验证
        String dbPassword = "$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi";
        boolean matches = encoder.matches(rawPassword, dbPassword);
        System.out.println("密码验证结果: " + matches);
        
        // 测试不同密码
        boolean wrongMatch = encoder.matches("123456", dbPassword);
        System.out.println("错误密码验证结果: " + wrongMatch);
    }
}