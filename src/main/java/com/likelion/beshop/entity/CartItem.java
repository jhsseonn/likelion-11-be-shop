package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
@Getter
@Setter
@ToString
public class CartItem extends BaseEntity{

    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private  Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private  Item item;

    private  int num;

    // CartItem 생성자 함수
    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setNum(count);
        return cartItem;
    }

    // count 매개변수로 받아서 cartItem count 값 올려주기
    public void addCount(int count){
        this.num += count;
    }

}

