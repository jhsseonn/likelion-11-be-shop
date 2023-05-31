package com.likelion.beshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}") // Spring의 @Value 어노테이션을 사용하여 주입된 uploadPath 값이 메서드의 인수로 사용
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") //리소스 핸들러에 대한 URL 패턴을 정의하는 인수
                .addResourceLocations(uploadPath);
    }
}