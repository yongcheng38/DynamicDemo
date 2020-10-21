package com.kanq.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: 配置允许全局跨域访问
 * @Date: 2020-09-29 14:03
 * @Author: yyc
 */
@Configuration
public class CorsConfig  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry  registry) {
        registry.addMapping("/**")
                //服务器允许的请求头
                .allowedHeaders("*")
                //服务器允许的请求方法
                .allowedMethods("POST", "PUT", "GET", "OPTIONS", "DELETE")
                //允许带 cookie 的跨域请求
                .allowCredentials(true)
                //服务端允许哪些域请求资源
                .allowedOrigins("*")
                //预检请求的缓存时间
                .maxAge(3600);

    }

}
