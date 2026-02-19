package com.test.takeout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护（开发环境）
            .csrf(AbstractHttpConfigurer::disable)
            
            // 配置CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 配置请求授权规则
            .authorizeRequests(authorize -> authorize
                // 允许匿名访问的接口
                .requestMatchers("/user/login").permitAll()
                .requestMatchers("/user/register").permitAll()
                .requestMatchers("/user/code").permitAll()
                .requestMatchers("/user/updatePassword").permitAll()
                .requestMatchers("/employee/login").permitAll()
                .requestMatchers("/employee/logout").permitAll()
                .requestMatchers("/common/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/error").permitAll()
                
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )
            
            // 禁用表单登录
            .formLogin(AbstractHttpConfigurer::disable)
            
            // 禁用HTTP基本认证
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * CORS配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许的来源
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        
        // 允许的HTTP方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 允许的请求头
        configuration.setAllowedHeaders(List.of(
            "Origin", "Content-Type", "Accept", "Authorization",
            "X-Requested-With", "Access-Control-Allow-Origin"
        ));
        
        // 允许携带凭证
        configuration.setAllowCredentials(true);
        
        // 预检请求的有效期
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
