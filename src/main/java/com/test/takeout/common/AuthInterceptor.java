package com.test.takeout.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 认证拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 拦截请求，验证token
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");

        // 检查token是否存在
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"code\": 0, \"msg\": \"未授权访问\", \"data\": null}");
            return false;
        }

        // 检查token格式
        if (!token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"code\": 0, \"msg\": \"token格式错误\", \"data\": null}");
            return false;
        }

        // 提取token
        String tokenValue = token.substring(7);

        // 验证token
        if (!JwtUtil.validateToken(tokenValue)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"code\": 0, \"msg\": \"token无效\", \"data\": null}");
            return false;
        }

        // 将用户ID存入请求属性
        Long userId = JwtUtil.getUserIdFromToken(tokenValue);
        request.setAttribute("userId", userId);

        return true;
    }

}
