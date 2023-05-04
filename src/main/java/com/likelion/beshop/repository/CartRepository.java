package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
//    @Autowired
//    private MemberRepository memberRepository;
//    Cart findById(Long member_id);
}
