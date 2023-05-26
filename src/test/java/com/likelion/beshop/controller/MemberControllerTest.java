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
    private PasswordEncoder passwordEncoder;
    public void createMember(String email, String password){
        Member member = new Member();
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("수빈");
        memberFormDto.setEmail(email);
        memberFormDto.setPwd(password);
        memberFormDto.setAddress("남양주");
        member = member.createMember(memberFormDto, passwordEncoder);
        memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email="qwe@qwe.qwe";
        String pwd="qwe123";
        this.createMember(email, pwd);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(pwd))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email="gd@gd.gd";
        String pwd="qwer";
        this.createMember(email, pwd);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password("hi"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}