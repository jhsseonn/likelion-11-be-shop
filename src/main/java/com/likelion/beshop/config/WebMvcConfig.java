package com.likelion.beshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")//어노테이션을 사용하여 application.properties 파일에서 uploadPath 값을 읽어와서 String 변수인 uploadPath에 할당
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) { //addResourceHandlers() 메소드를 오버라이드하여, ResourceHandlerRegistry 객체를 통해 정적 리소스 요청을 처리하기 위한 설정을 추가
        registry.addResourceHandler("/images/**") //"/images/" 경로로 시작하는 URL 요청을 정적 리소스 요청으로 처리하도록 합니다. **는 하위 경로까지 모두 매핑하도록 설정
                .addResourceLocations(uploadPath); //정적 리소스가 위치한 디렉토리 경로를 등록
    }
}