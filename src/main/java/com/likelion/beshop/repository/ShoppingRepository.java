package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Shopping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingRepository extends JpaRepository<Shopping,Long> {
}
