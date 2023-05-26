package com.likelion.beshop.entity;

import com.likelion.beshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter @ToString
public class Order extends BaseEntity{
    @Id
    @Column(name="order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    private LocalDateTime orderTime;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderTime(LocalDateTime.now());

        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
