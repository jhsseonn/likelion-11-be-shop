package com.likelion.beshop.repository;

import com.likelion.beshop.dto_.MemberFormDto;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartRepositoryTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartRepository cartRepository;
    @PersistenceContext
    EntityManager em;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("ghs");
        memberFormDto.setEmail("ghs@naver.com");
        memberFormDto.setPassword("1234");
        memberFormDto.setAddress("마포구");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void cartTest(){
        Member member = createMember(); //회원 엔티티 생성해서 거기에 메소드 호출한거
        memberRepository.save(member); //거기에 저장

        Cart cart = new Cart(); // 카트 생성
        cart.setMember(member);// 카트 생성한거에 위에 만든 회원을 집어 넣음
        cartRepository.save(cart);// 카트 생성한거 저장

        em.flush(); //영속성 컨텍스트에 데이터 저장 후 flush()를 통해 데베에 반영 -> 강제 반영
        em.clear(); //영속성 컨텍스트에 엔티티가 없을 경우 데베 조회 -> 데베 확인 위해 clear로 비워주기

        Cart savedCart = cartRepository.findById(cart.getId()) // 지정된 장바구니 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(savedCart.getMember().getId(), member.getId()); //처음 저장했던 멤버 id와 조회한 장바구니에서의 멤버 id가 동일한지 asserEquals로 확인
    }
}