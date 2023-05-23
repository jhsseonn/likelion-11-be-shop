package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.constant.Role;
import com.likelion.beshop.dto.MemberFormDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.persistence.EntityNotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    private Object EntityNotFoundException;

    public Member createMember()
    {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("양수빈");
        memberFormDto.setEmail("pppppark2@gmail.com");
        memberFormDto.setAddress("경기도 남양주시");
        memberFormDto.setPwd("qwer1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원 생성 테스트")
    public void createNewMember(){
        Member member = createMember();
        memberRepository.save(member);
        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        entityManager.flush();
        entityManager.clear();

        Cart tmpCart = cartRepository.findById(cart.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(tmpCart.getMember().getId(), member.getId());
    }
}