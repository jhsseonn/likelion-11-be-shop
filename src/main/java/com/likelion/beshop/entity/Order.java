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
@Table(name="orders") // order는 예약어라 orders
@Getter
@Setter
@ToString
public class Order extends BaseEntity {

    // 주문 코드
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 주문 회원필드를 Member 엔티티에 매핑
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name="member_id")
    private Member member;

    // 주문 상태 => enum
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 주문일
    private LocalDateTime orderDate;

    // BaseEntity가 등록일, 수정일, 등록자, 수정자를 모두 갖고 있어서 등록시간, 수정시간 멤버변수는 삭제
//    // 등록 시간
//    private LocalDateTime registerTime;
//    // 수정 시간
//    private LocalDateTime updateTime;

    // OrderItem과 연관 관계 매핑 추가, 영속성 전이 옵션(All)
    //@OneToMany(mappedBy="order", cascade=CascadeType.ALL) // => LAZY옵션이 없으므로 OrderTest에서 실행하면 Item, orderItem이 3번 insert 됨(order는 1번 insert)
    //@OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval = true) // 고아 객체 제거 옵션 추가

    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)  // 즉시 로딩이 아닌 지연 로딩 방식
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);

        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDERING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
