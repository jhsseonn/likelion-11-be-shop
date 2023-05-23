package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="cart_item")
@Getter
@Setter
public class CartItem extends Base {

    @Id
    @Column(name="cart_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // PK

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart; // FK

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item; // FK

    @NotNull
    private Integer count;

    // CartItem 객체 생성 (카트 객체에 아이템을 카트 아이템으로 바꾸어 담아줌)
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);

        return cartItem;
    }

    // 장바구니 아이템 개수 증가
    public void addCount(int count) {
        this.count += count;
    }
}
