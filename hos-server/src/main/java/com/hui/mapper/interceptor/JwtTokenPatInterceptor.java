package com.hui.mapper.interceptor;


import com.hui.constant.JwtClaimsConstant;
import com.hui.context.BaseContext;
import com.hui.properties.JwtProperties;
import com.hui.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 患者jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenPatInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        log.info("=== JWT 拦截器调试信息 ===");

        // 打印所有请求头信息
        log.info("所有请求头信息:");
        request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
            log.info("Header - {}: {}", headerName, request.getHeader(headerName));
        });

        // 检查 JwtProperties 配置
        log.info("配置的 patTokenName: {}", jwtProperties.getPatTokenName());

        if (jwtProperties.getPatTokenName() == null) {
            log.error("patTokenName 配置为空，请检查配置文件");
            response.setStatus(401);
            return false;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getPatTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getPatSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.PAT_ID).toString());
            log.info("当前患者id：{}", userId);

            //将患者id设置到ThreadLocal中,重点
            BaseContext.setCurrentId(userId);

            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
