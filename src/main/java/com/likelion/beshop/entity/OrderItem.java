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
    @Column(name="OrderItem_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Order_id")
    private  Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int price;

    private int num;

//    private LocalDateTime time;
//
//    private LocalDateTime editTime;

    public  static  OrderItem createOrderItem(Item item, int num){ // 주문하기 위해 OrderItem을 생성하는 만큼, 매개변수는 _item_과 _num(주문수량)_
        OrderItem orderItem = new OrderItem();

        // OrderItem 상품과 수량 세팅
        orderItem.setItem(item);
        orderItem.setNum(num);

        orderItem.setPrice(item.getPrice());
        item.removeStock(num); // 위에서 생성한 함수로 재고 수량 조정
        return  orderItem;
    }
    public  int getTotalPrice() {
        return price * num; // _orderPrice_와 _num_를 곱하여 주문 가격 계산값을 반환
    }
}
