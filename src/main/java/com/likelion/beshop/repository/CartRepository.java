package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberId(Long memberId); // 멤버 아이디로 장바구니 조회

}
