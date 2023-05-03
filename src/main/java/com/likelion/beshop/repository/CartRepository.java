package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {


}
