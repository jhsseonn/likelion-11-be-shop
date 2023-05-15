package com.likelion.beshop.entity;

import com.likelion.beshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.likelion.beshop.entity.QOrderItem.orderItem;

@Entity
@Table(name="orders") // mysql 예약어 order
@Getter
@Setter
public class Order extends Base {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // PK

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // FK

    // 주문 시간
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY) // Order 엔티티가 연관관계의 주인이 됨
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        // orderItems 배열에 추가

        orderItems.add(orderItem);
        // orderItem의 order도 현재 order로 지정
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        // 사용자 세팅
        order.setMember(member);
        // 주문 상품 배열 돌며 orderItems 배열에 주문 상품 추가
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        // 주문 상태 세팅
        order.setOrderStatus(OrderStatus.ORDER);

        // BaseTime의 regTime(등록시간)을 써도되지 않을까..?
        order.setOrderDate(LocalDateTime.now());

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

