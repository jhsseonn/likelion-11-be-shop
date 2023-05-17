package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.dto.OrderFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@ToString
public class Order extends BaseEntity{

    @Id
    @Column(name="order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    //LocalDateTime로 하는 게 맞음
    //주문일
    private LocalDateTime date;

    //주문상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderstatus;

    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;

    @OneToMany(mappedBy="orders", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){

        orderItems.add(orderItem); //주문 객체에 주문 상품 객체 연결
        orderItem.setOrders(this); // 주문 상품 객체에 주문 객체 연결(연관관계 주인)
    }


    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderstatus(OrderStatus.ORDER);
        order.setDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;

        //각 상품마다 TotalPrice 를 구하고 모두 더함
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
