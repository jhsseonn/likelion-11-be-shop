package com.likelion.beshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    //application.properties 파일에서 uploadPath 설정 값을 가져와 변수에 넣는 어노테이션
    @Value("${uploadPath}")
    String uploadPath;

    //자신의 로컬 컴퓨터에 업로드한 파일을 찾을 위치 설정하는 메소드
    //ResourceHandlerRegistry 정적 리소스 처리 위한 레지스트리
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //패턴을 등록하고 패턴의 정적 리소스의 실제 위치를 지정함
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
