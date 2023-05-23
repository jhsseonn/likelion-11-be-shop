package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Order;
import com.likelion.beshop.entity.OrderItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findById(Order order);

    // 현재 로그인한 회원의 주문 데이터를 페이징 조건에 맞춰 조회
    @Query("select o from Order o " + // Order 객체에서
            "where o.member.email = :email " + // 파라미터의 이메일과 같은 이메일을 가진 멤버의
            "order by o.orderDate desc" // 주문 내역을 날짜 역순으로 정렬
    )
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    // 현재 로그인한 회원의 주문 수를 조회
    @Query("select count(o) from Order o " + // Order 테이블에서 해당 주문 데이터 수를 조회
            "where o.member.email = :email" // 파라미터의 이메일과 같은 이메일을 가진 멤버의
    )
    Long countOrder(@Param("email") String email);

}
