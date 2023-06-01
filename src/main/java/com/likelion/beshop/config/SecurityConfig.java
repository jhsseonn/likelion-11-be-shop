package com.likelion.beshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()//시큐리티에서 제공하는 인증방식
                .loginPage("/members/login")//로그인 페이지 주소가 여기라는 뜻
                .defaultSuccessUrl("/")//성공하면 메인페이지로 돌아감
                .usernameParameter("email")//이메일지정 아이디 파라미터명 지정
                .failureUrl("/members/login/error")//로그인 실패시 저 주소로 매핑해둔 것
                .and()
                .logout()//로그아웃 처리
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))//로그아웃 처리했을 때의 주소
                .logoutSuccessUrl("/");//로그인 성공했을 때 메인페이지로 돌아가게 해둠

        http.authorizeRequests()//보안 검사
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()//모든 사용자한테 허락을 한다는 것, 다 보여준다
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")//관리자 페이지, hasRole을 사용하여 ADMIN을 가진사람만 접근 가능하게해둠
                .anyRequest().authenticated();//접근을 제한하는 것

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());//인증 실패시 어떻게 대처할지 써둠

        return http.build();
    }

}