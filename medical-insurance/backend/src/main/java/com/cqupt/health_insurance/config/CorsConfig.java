package com.cqupt.health_insurance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 创建CORS配置对象
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端域名访问（生产环境需指定具体域名，如"http://localhost:8080"）
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost:8082");
        config.addAllowedOrigin("http://localhost:8083");
        // 允许携带Cookie（前后端分离场景需开启）
        config.setAllowCredentials(true);
        // 允许所有请求方法（GET、POST、PUT、DELETE等）
        config.addAllowedMethod("*");
        // 允许所有请求头（如Content-Type、Authorization等）
        config.addAllowedHeader("*");
        // 暴露响应头中的信息（如自定义头，可选）
        config.addExposedHeader("*");
        // 设置预检请求的有效期（单位：秒，避免频繁预检请求）
        config.setMaxAge(3600L);

        // 2. 配置CORS路径匹配规则
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口生效（包括子路径，如/medicalservice/*、/captcha等）
        source.registerCorsConfiguration("/**", config);

        // 3. 返回CORS过滤器
        return new CorsFilter(source);
    }
}