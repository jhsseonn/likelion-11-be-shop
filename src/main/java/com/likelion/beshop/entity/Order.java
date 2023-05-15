package com.likelion.beshop.entity;

import com.likelion.beshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@ToString
public class Order extends BaseEntity{
    @Id
    @Column(name="Order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private  Member member;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

//    private LocalDateTime time;
//
//    private LocalDateTime editTime;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order(); // 주문 객체 생성 후
        order.setMember(member); // 회원 세팅

        // 장바구니에서는 한 개의 상품이 아닌 여러 개의 상품을 주문하므로, 리스트 형태로 주문 객체에 orderItem 객체를 추가
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER); // 현재 주문 상태는 ‘주문’ 으로 세팅
        order.setDate(LocalDateTime.now()); // LocalDateTime으로 주문 시각 세팅
        return order;
    }

    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice +=orderItem.getTotalPrice(); // OrderItem 메소드 사용하여 주문 금액 계산
        }
        return totalPrice;
    }
}