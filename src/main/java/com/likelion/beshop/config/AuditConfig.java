package com.likelion.beshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA의 Auditing 기능 활성화
public class AuditConfig {

    // 등록자와 수정자를 현재 로그인한 사용자로 지정해주는 AuditorAwareImpl를 빈으로 등록
    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }
}