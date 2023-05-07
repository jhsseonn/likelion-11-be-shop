package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static com.likelion.beshop.entity.Member.createMember;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입테스트~")
    public void createMemberTest()
    {
        Member member = new Member();
        Member tmp;

        member.setName("soobin");
        member.setEmail("pppppark2@gmail.com");
        member.setPwd("qwer1234");
        member.setAddress("남양주");
        tmp=memberRepository.save(member);
        System.out.println(member.toString());
    }

}
