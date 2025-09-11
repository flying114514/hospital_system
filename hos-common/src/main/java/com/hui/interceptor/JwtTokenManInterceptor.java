package com.hui.interceptor;


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
 * 医生jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenManInterceptor implements HandlerInterceptor {

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

        //1、从请求头中获取管理员令牌
        String token = request.getHeader(jwtProperties.getManTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getManSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.MAN_ID).toString());
            log.info("当前管理员id：{}", empId);

            //将医生id设置到ThreadLocal中,重点
            BaseContext.setCurrentId(empId);

            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
