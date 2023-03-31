package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트") //무엇에 대한 테스트인지 적어줌
    public void createMemberTest(){
        Member member = new Member();
        member.setName("양수빈");
        member.setEmail("pppppark2@gmail.com");
        member.setPwd("qwer1234@@");
        member.setAddress("경기도 남양주시 오남읍");
        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());
    }
}


