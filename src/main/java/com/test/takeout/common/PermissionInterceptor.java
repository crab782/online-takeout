package com.test.takeout.common;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PermissionInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取当前用户信息
        // 2. 获取请求路径
        // 3. 验证用户权限
        // 4. 验证资源归属
        // 5. 返回验证结果
        return true;
    }
}
