package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orders_item")
@Getter @Setter @ToString
public class OrderItem extends BaseEntity{
    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
    private Integer price;
    private Integer count;

//    public static OrderItem createOrderItem(Item item, int quantity) {
//        OrderItem orderItem = new OrderItem();
//        orderItem.setItem(item);
//        orderItem.setCount(quantity);
//        item.removeStock(quantity);
//        return orderItem;
//    }

    public int getTotalPrice() {
        return item.getPrice() * count;
    }
}
