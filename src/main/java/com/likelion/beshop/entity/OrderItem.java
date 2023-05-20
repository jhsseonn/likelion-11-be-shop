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
public class OrderItem extends BaseEntity{

    @Id
    @Column(name="orderItem_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //주문 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="items_id")
    private Item item;

    //주문코드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orders_id")
    private Order orders;

    //주문가격
    private int price;

    //수량
    private int count;

    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;

    public static OrderItem createOrderItem(Item item, int count){

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return price * count;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }


}
