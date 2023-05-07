package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartRepositoryTest {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

    public Member createMemberEntity() {
        Member member = new Member();
        member.setName("한다은");
        member.setEmail("daeunhan0723@gmail.com");
        member.setPassword("password");
        member.setAddress("서울시 양천구 목동");
        Member savedMember = memberRepository.save(member);
        System.out.println(savedMember.toString());
        return member;
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 조회 테스트")
    public void cartMemberTest() {

        Member member = createMemberEntity();
        this.memberRepository.save(member);

        Cart cart = new Cart();
        cart.setMember(member);
        cartRepository.save(cart);

        entityManager.flush();
        entityManager.clear();

        Cart retrievedCart = entityManager.find(Cart.class, cart.getCode());

        assertEquals( member.getId(), retrievedCart.getMember().getId());

    }

}