package com.hui.config;

import com.hui.mapper.interceptor.JwtTokenPatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenPatInterceptor jwtTokenPatInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenPatInterceptor)
                .addPathPatterns("/user/main/**")  // 拦截/user开头的请求
                .excludePathPatterns("/user/main/login"); // 排除登录接口
    }
}