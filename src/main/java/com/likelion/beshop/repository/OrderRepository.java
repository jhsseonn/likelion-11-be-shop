package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Order;
import com.likelion.beshop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(Order order);
}
