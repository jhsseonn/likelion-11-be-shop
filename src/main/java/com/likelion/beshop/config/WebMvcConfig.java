package com.likelion.beshop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 파일 처리하는 WebMvcConfig 클래스
// WebMvcConfig 인터페이스를 구현하고 addResourceHandle메소드를 통해 자신의 로컬 컴퓨터에 업로드한 파일을 찾을 위치 설정
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}") // application.properties에 설정한 uploadPath 프로퍼티 값 읽어옴
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**") // 웹 브라우저에 입력하는 url에 /images로 시작하는 경우 uploadPath에 설정한 폴더를 기준으로 파일을 읽어오게 설정
                .addResourceLocations(uploadPath); // 로컬 컴퓨터에 저장된 파일을 읽어올 root 경로 설정
    }
}