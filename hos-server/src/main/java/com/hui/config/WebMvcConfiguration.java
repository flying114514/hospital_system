package com.hui.config;


import com.hui.interceptor.JwtTokenDocInterceptor;
import com.hui.interceptor.JwtTokenManInterceptor;
import com.hui.interceptor.JwtTokenPatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenPatInterceptor jwtTokenPatInterceptor;


    @Autowired
    private JwtTokenDocInterceptor jwtTokenDocInterceptor;

    @Autowired
    private JwtTokenManInterceptor jwtTokenManInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 配置患者端拦截器
        registry.addInterceptor(jwtTokenPatInterceptor)
                .addPathPatterns("/user/main/**")  // 拦截/user开头的请求
                .excludePathPatterns("/user/main/login"); // 排除登录接口

        // 配置医生端拦截器
        registry.addInterceptor(jwtTokenDocInterceptor)
                .addPathPatterns("/doc/main/**")
                .excludePathPatterns("/doc/main/login")
                .excludePathPatterns("/doc/main/update");

        // 配置管理员端拦截器
        registry.addInterceptor(jwtTokenManInterceptor)
                .addPathPatterns("/manager/**")
                .excludePathPatterns("/manager/login");
    }
}