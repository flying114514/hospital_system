package com.hui.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hos.jwt")
@Data
public class JwtProperties {

    /**
     * 医生端用户生成jwt令牌相关配置
     */
    private String docSecretKey;
    private long docTtl;
    private String docTokenName;

    /**
     * 患者端用户生成jwt令牌相关配置
     */
    private String patSecretKey;
    private long patTtl;
    private String patTokenName;


    /**
     * 管理员端用户生成jwt令牌相关配置
     */
    private String manSecretKey;
    private long manTtl;
    private String manTokenName;

}
