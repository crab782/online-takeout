package com.test.takeout.config;

import com.test.takeout.common.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/backend/**") // 拦截所有后端接口
                .excludePathPatterns("/backend/user/login") // 排除登录接口
                .excludePathPatterns("/backend/user/logout") // 排除登出接口
                .excludePathPatterns("/backend/user/info") // 排除获取用户信息接口
                .excludePathPatterns("/employee/login") // 排除员工登录接口
                .excludePathPatterns("/employee/logout"); // 排除员工登出接口
    }
}
