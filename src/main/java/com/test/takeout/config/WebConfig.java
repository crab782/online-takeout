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
                .addPathPatterns("/employee/**") // 拦截员工相关接口
                .addPathPatterns("/activity/**") // 拦截活动相关接口
                .addPathPatterns("/shoppingCart/**") // 拦截购物车相关接口
                .addPathPatterns("/order/**") // 拦截订单相关接口
                .addPathPatterns("/addressBook/**") // 拦截地址簿相关接口
                .addPathPatterns("/user/info") // 拦截获取用户信息接口
                .addPathPatterns("/user/password") // 拦截修改密码接口
                .addPathPatterns("/user/update") // 拦截更新用户信息接口
                .excludePathPatterns("/user/login") // 排除登录接口
                .excludePathPatterns("/user/register") // 排除注册接口
                .excludePathPatterns("/user/code") // 排除获取验证码接口
                .excludePathPatterns("/user/updatePassword") // 排除更新密码接口
                .excludePathPatterns("/user/loginout") // 排除登出接口
                .excludePathPatterns("/employee/login") // 排除登录接口
                .excludePathPatterns("/employee/logout"); // 排除登出接口
    }

}
