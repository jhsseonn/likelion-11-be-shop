package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart_item")
@Getter @Setter @ToString
public class CartItem {
    @Id
    @Column(name="cart_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne()
    @JoinColumn(name="cart_id")
    private Cart cart;
    @ManyToOne()
    @JoinColumn(name="item_id")
    private Item item;
    private Integer count;
}
