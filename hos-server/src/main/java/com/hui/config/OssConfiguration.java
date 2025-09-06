package com.hui.config;


import com.hui.properties.AliOssProperties;
import com.hui.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置类,创建AilOssUtil对象
@Configuration
@Slf4j
public class OssConfiguration {

    //根据已经注入到容器中的AliOssProperties对象,使用其中的四个属性,创建AliOssUtil对象
    @Bean//将AliOssUtil对象注入到容器中,一启动就会创建这个对象
    @ConditionalOnMissingBean//当容器中不存在AliOssUtil对象时,才会创建AliOssUtil对象
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象,参数:{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName());

    }
}
