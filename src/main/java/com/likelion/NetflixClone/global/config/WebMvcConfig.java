package com.likelion.NetflixClone.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 요청을 프로젝트 루트의 uploads/ 디렉토리로 매핑
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/");
    }
}