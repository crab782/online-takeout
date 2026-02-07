package com.test.takeout.common;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取Token
        // 2. 验证Token有效性
        // 3. 解析用户信息
        // 4. 将用户信息存入请求上下文
        // 5. 返回验证结果
        return true;
    }
}
