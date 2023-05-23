package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter
@Setter
public class Cart extends Base {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // PK

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // FK

    // 변수명에는 언더바 사용 불가

    // 카트 객체 생성
    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.member = member;
        return cart;
    }
}