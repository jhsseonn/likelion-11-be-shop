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
public class Order {
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

    private LocalDateTime time;

    private LocalDateTime editTime;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
}