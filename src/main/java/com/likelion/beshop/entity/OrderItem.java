package com.likelion.beshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="order_item")
@Getter
@Setter
public class OrderItem extends BaseEntity{
    // 주문 상품 코드
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    // 주문 상품 필드를 Item 엔티티와 매핑
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name = "item_id")
    private Item item;

    // 주문 코드 필드를 Order 엔티티에 매핑
    @ManyToOne(fetch = FetchType.LAZY) // 즉시 로딩이 아닌 지연 로딩 방식
    @JoinColumn(name = "order_id")
    private Order order;

    // 주문 가격
    private int orderPrice;

    // 수량
    private int count;

    // BaseEntity가 등록일, 수정일, 등록자, 수정자를 모두 갖고 있어서 등록시간, 수정시간 멤버변수는 삭제
//    // 등록 시간
//    private LocalDateTime registerTime;
//
//    // 수정 시간
//    private LocalDateTime updateTime;


    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStock(count);
        return orderItem;
    }

    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel() {
        this.getItem().addStock(count);
    }
}
