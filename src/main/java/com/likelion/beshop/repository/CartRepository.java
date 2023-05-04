package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
