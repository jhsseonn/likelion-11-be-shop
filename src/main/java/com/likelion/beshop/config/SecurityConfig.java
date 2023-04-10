package com.likelion.beshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login") // 참고로 url 매핑은 controller 에서 함
                .defaultSuccessUrl("/")  //성공하면 메인페이지로
                .usernameParameter("email")
                .failureUrl("/members/login/error") //에러일 때
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 처리 할 때 주소
                .logoutSuccessUrl("/"); //로그아웃 성공하면 메인페이지로

        http.authorizeRequests() //보안 검사하는 부분
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll() //모든 사용자에게 허락
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN") // 어드민 권한으로 회원가입 한 사람만 접근 가능하게
                .anyRequest().authenticated(); // 접근 제한

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()); // 인증 실패 커스텀 가능

        return http.build();
    }

}