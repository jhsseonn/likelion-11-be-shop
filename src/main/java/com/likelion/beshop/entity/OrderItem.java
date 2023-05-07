package com.likelion.beshop.entity;

import com.likelion.beshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orderItem")
@Getter
@Setter
@ToString
public class OrderItem {
    @Id
    @Column(name="OrderItem_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long code;

    @ManyToOne
    @JoinColumn(name = "Order_id")
    private  Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int price;

    private int num;

    private LocalDateTime time;

    private LocalDateTime editTime;
}
