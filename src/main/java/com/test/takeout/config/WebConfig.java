package com.test.takeout.config;

import com.test.takeout.common.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns("/user/login") // 排除登录接口
                .excludePathPatterns("/user/register") // 排除注册接口
                .excludePathPatterns("/user/code") // 排除获取验证码接口
                .excludePathPatterns("/user/updatePassword") // 排除更新密码接口
                .excludePathPatterns("/user/loginout") // 排除登出接口
                .excludePathPatterns("/employee/login") // 排除登录接口
                .excludePathPatterns("/employee/logout") // 排除登出接口
                .excludePathPatterns("/platform/login") // 排除平台登录接口
                .excludePathPatterns("/platform/logout") // 排除平台登出接口
                .excludePathPatterns("/platform/info") // 排除平台信息接口
                .excludePathPatterns("/backend/user/login") // 排除后端登录接口
                .excludePathPatterns("/backend/user/logout") // 排除后端登出接口
                .excludePathPatterns("/backend/user/info") // 排除后端用户信息接口
                .excludePathPatterns("/store/**") // 排除店铺接口
                .excludePathPatterns("/shop/**") // 排除shop接口
                .excludePathPatterns("/category/**") // 排除分类接口
                .excludePathPatterns("/dish/**") // 排除菜品接口
                .excludePathPatterns("/setmeal/**") // 排除套餐接口
                .excludePathPatterns("/common/**") // 排除公共接口
                .excludePathPatterns("/error") // 排除错误页面
                .excludePathPatterns("/test/**"); // 排除测试接口
    }
}
