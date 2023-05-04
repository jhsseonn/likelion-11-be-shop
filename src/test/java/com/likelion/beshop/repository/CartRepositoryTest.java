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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("1son");
        memberFormDto.setEmail("1son@tistory.com");
        memberFormDto.setPassword("1234");
        memberFormDto.setAddress("서울시 ");
        return Member.createMember(memberFormDto,passwordEncoder);
        //Member savedMember = memberRepository.save(member);
        //System.out.println(savedMember.toString());
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCarteAndMemberTest(){

        Member member = createMember();
        this.memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        em.flush();
        em.clear();

        Cart savedCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getId(), member.getId());

    }



}