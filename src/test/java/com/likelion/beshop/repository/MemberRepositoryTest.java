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

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("회원가입 테스트")
    public void createMemberTest() {
        Member member = new Member();
        member.setName("sjK");
        member.setEmail("sj@naver.com");
        member.setPassword("sj23");
        member.setAddress("서울특별시 강서구");
        Member saveMember = memberRepository.save(member); // save()함수 잘 동작하는지 확인하기 위해 saveMember 객체 생성
        System.out.println(saveMember.toString());

    }

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "seongJu", roles="USER")
    public void auditingTest() {
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("register time : " + member.getRegTime());
        System.out.println("update time : " + member.getUpdateTime());
        System.out.println("creater : " + member.getCreatedBy());
        System.out.println("modifier : " + member.getModifiedBy());

    }
}
