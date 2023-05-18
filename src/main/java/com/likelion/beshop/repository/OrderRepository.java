package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // ‘사용자의 주문 데이터를 조회하여 날짜 역순으로 정렬’하는 쿼리
    @Query("select o from Order o " +
            "where o.member.email = :email " +
            "order by o.date desc"
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable); // 현재 로그인한 회원의 주문 데이터를 페이징 조건에 맞춰 조회

    // ‘사용자의 주문 데이터 수를 조회’하는 쿼리
    @Query("select count(o) from Order o " +
            "where o.member.email = :email"
    )
    Long countOrder(@Param("email") String email); // 현재 로그인한 회원의 주문 수를 조회
}
