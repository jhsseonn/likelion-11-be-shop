package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="order_item")
@Getter
@Setter
public class OrderItem extends BaseEntity  {

    @Id
    @Column(name="order_item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orders_id") //질문
    private Order order;

    private int price;

    private int count;

    //private LocalDateTime uploadTime;

    //private LocalDateTime editTime;


    public static OrderItem createOrderItem(Item item, int count)
    {
        OrderItem orderItem = new OrderItem(); //orderItem 객체 생성
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return price* count;
    }

    public void cancel(){
        this.getItem().addStock(count);
    }
}
