package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
@Getter
@Setter
public class CartItem extends BaseEntity{
    // 장바구니 상품 번호
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_item_id")
    private Long id;

    // 장바구니 번호 (FK) => 다대일
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name = "cart_id")// 외래키 이름
    private Cart cart;

    // 상품 번호 (FK) => 다대일
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name = "item_id") // 외래키 이름
    private Item item;

    //상품 개수; 같은 상품 여러 개 주문 가능
    @Column(nullable = false)
    private int count;

}
