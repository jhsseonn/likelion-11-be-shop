package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberRepositoryTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Test
    @DisplayName("회원가입 테스트")
    public void createMemberTest(){
        Member member = new Member();
        member.setName("박서윤");
        member.setEmail("fjqmqjrm@naver.com");
        member.setPassword("fjqmqjrm123");
        member.setAddress("수원시 장안구 정자동");
        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());

    }

}
