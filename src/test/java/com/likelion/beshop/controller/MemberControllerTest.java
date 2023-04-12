package com.likelion.beshop.controller;

import com.likelion.beshop.dto_.MemberFormDto;
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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

//import javax.transaction.Transactional;



@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations ="classpath:application-test.properties")
class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;



    public Member createMember(String email, String pwd) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("구혜승");
        memberFormDto.setAddress("서울시 마포구");
        memberFormDto.setPassword(pwd);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "amu6675@naver.com";
        String pwd = "ghs66756675";
        this.createMember(email, pwd);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(pwd))
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "amu6675@naver.com";
        String pwd = "ghs66756675";
        this.createMember(email, pwd);

        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login/error")
                        .user(email).password("ghsghs6675"))
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}