package com.test.takeout.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF保护
            .csrf(AbstractHttpConfigurer::disable)
            
            // 禁用CORS限制（由代理处理）
            .cors(AbstractHttpConfigurer::disable)
            
            // 允许所有请求
            .authorizeRequests(authorize -> authorize
                .anyRequest().permitAll()
            )
            
            // 禁用表单登录
            .formLogin(AbstractHttpConfigurer::disable)
            
            // 禁用HTTP基本认证
            .httpBasic(AbstractHttpConfigurer::disable)
            
            // 添加CORS过滤器
            .addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 简单的CORS过滤器
     */
    public static class CorsFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                        FilterChain filterChain) throws ServletException, IOException {
            // 设置CORS头
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Max-Age", "3600");
            
            // 对于OPTIONS预检请求，直接返回200
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            
            filterChain.doFilter(request, response);
        }
    }
}
