//공통 속성 공통화
package com.likelion.beshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
//@EnableJpaAuditing 지정하여 Auditing 기능 활성화
//"auditorProvider" 이름으로 AuditorAware 구현체 Bean 등록
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }
}