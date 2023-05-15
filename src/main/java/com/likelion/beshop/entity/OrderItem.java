package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="order_item")
@Getter
@Setter
public class OrderItem extends Base {
    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // PK

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private Integer orderPrice; // 가격
    private Integer count; // 주문 수량

    // OrderItem 객체 생성
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice()); // 상품 가격 불러오기

        // 재고 감소
        // item.removeStock(count);
        return orderItem;
    }

    // 주문 상품 총 가격  반환
    public int getTotalPrice() {
        return count * orderPrice;
    }

}
