package com.likelion.beshop.repository;

import com.likelion.beshop.constant.Role;
import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    // 회원 엔티티 생성
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setName("최유신");
        memberFormDto.setEmail("cys74351@swu.ac.kr");
        memberFormDto.setPassword("1234");
        memberFormDto.setAddress("인천광역시");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartMemberTest() {
        Member member = createMember(); // 회원 엔티티 생성
        memberRepository.save(member); // 회원 저장

        Cart cart = new Cart(); // 카트 엔티티 생성
        cart.setMember(member); // 회원 지정
        cartRepository.save(cart); // 카트 저장

        em.flush(); // 데이터베이스에 반영 (강제 반영)
        em.clear(); // 영속성 컨텍스트에 데이터가 없을 경우 DB 조회 위해 clear로 비워줌

        // 저장된 카트 엔티티를 id로 조회
        Cart savedCart = cartRepository.findById(cart.getId()).orElseThrow(EntityNotFoundException::new);
        // 조회된 카트의 멤버 id가 처음 저장한 멤버의 id와 같은지 비교
        assertEquals(savedCart.getMember().getId(), member.getId());
    }

    // 회원 엔티티 생성
}
