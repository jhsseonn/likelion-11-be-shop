package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="order_item")
@Getter
@Setter
public class OrderItem {
    // 주문 상품 코드
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    // 주문 상품 필드를 Item 엔티티와 매핑
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name = "item_id")
    private Item item;

    // 주문 코드 필드를 Order 엔티티에 매핑
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name = "order_id")
    private Order order;

    // 주문 가격
    private int price;

    // 수량
    private int count;

    // 등록 시간
    private LocalDateTime registerTime;

    // 수정 시간
    private LocalDateTime updateTime;

}
