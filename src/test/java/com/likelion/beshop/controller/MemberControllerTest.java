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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("최유신");
        memberFormDto.setPassword(password);
        memberFormDto.setAddress("인천광역시");

        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    // 로그인 성공 테스트
    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@naver.com";
        String password = "qwerty";
        this.createMember(email, password);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password(password)) // 멤버의 email과 pw 넣어줌
                .andExpect(SecurityMockMvcResultMatchers.authenticated()); // formLogin을 통해 성공적으로 로그인되었다.
    }

    // 로그인 실패 테스트
    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailureTest() throws Exception {
        String email = "test@naver.com";
        String password = "qwerty"; // 성공 pw, 이메일과 다른 문자열 지정
        this.createMember(email, password);

        mockMvc.perform(formLogin().userParameter("email")
                .loginProcessingUrl("/members/login")
                .user(email).password("failure")) // 멤버에서 받아온 파라미터와 다른 pw를 넣어 실패하도록
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated()); // formLogin을 통해 로그인 실패 확인
    }

}
