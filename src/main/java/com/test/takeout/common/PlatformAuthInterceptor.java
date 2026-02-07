package com.test.takeout.common;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PlatformAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取Token
        // 2. 验证Token有效性
        // 3. 解析用户信息和角色
        // 4. 验证角色权限
        // 5. 将用户信息存入请求上下文
        // 6. 返回验证结果
        return true;
    }
}
