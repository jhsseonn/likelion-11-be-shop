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
import javax.transaction.Transactional;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

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
    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "구혜승", roles = "USER")
    public void auditingTest(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("register time : " +member.getRegTime());
        System.out.println("update time : "+ member.getUpdateTime());
        System.out.println("creater : " + member.getCreatedBy());
        System.out.println("modifier : " + member.getModifiedBy());
    }


}
