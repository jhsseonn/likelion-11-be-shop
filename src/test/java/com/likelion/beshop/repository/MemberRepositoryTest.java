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
    @DisplayName("회원가입테스트")
    public void createMemberTest() {
        Member member = new Member();
        member.setName("구혜승");
        member.setEmail("amu6675@naver.com");
        member.setPwd("ghs66756675");
        member.setAddress(("서울시 마포구"));
        Member savedMember = memberRepository.save(member);
        System.out.println((savedMember.toString()));
    }
}
