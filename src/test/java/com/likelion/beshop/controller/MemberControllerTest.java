package com.likelion.beshop.controller;

import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberControllerTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;

    // 멤버 객체 하나 만들기
    public Member createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구 합정동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto,passwordEncoder);
        return memberService.saveMember(member);
    }
    // 로그인 성공 테스트 코드 구현
    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@gmail.com";
        String password = "1234";
        // 해당 이메일, 비밀번호를 멤버 객체에 넣어주기
        this.createMember(email, password);

        // mockMvc 로그인 테스트 코드 구현하기
        // 이메일, 비밀번호를 일단 동일하게 설정
        mockMvc.perform(formLogin().userParameter("email")
            .loginProcessingUrl("/members/login")
                .user(email).password(password))
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); //andExpect가 비교검증
    }

    // 로그인 실패 테스트 코드 구현
    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailureTest() throws Exception {
        String email = "test@gmail.com";
        String password = "1234";
        // 해당 이메일, 비밀번호를 멤버 객체에 넣어주기
        this.createMember(email, password);

        // mockMvc 로그인 테스트 코드 구현하기
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login")
                        .user(email).password("4321")) // 비밀번호를 다르게 설정
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}