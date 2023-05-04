package com.likelion.beshop.repository;

import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.springframework.test.util.AssertionErrors.assertEquals;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    // 회원 엔티티 생성 메소드
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("sjK");
        memberFormDto.setEmail("sj@naver.com");
        memberFormDto.setPassword("sj23");
        memberFormDto.setAddress("서울특별시 강서구");
        return Member.createMember(memberFormDto, passwordEncoder);

    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void createMemberTest() {
        Member member = createMember(); // 회원 엔티티 생성 메소드 호출
        memberRepository.save(member); //회원 엔티티 저장

        Cart cart = new Cart(); // 카트 생성
        cart.setMember(member); // 카트에 회원 지정
        cartRepository.save(cart); // 카트 저장

        em.flush(); // DB에 강제 반영
        em.clear(); // 엔티티 없을 경우 DB 조회 -> DB 확인

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals("error",savedCart.getMember().getId(), member.getId());

    }
}
