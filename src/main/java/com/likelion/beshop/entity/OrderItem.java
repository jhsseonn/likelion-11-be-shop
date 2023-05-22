package com.likelion.beshop.entity;

import com.likelion.beshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="orderitems")
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "orderitem_id")
    private  Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item ; // 주문 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 주문 코드


    private int orderPrice;//주문 가격
    private int num; // 주문수량

    public static OrderItem createOrderItem(Item item, int num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setNum(num);
        orderItem.setOrderPrice(item.getPrice());
        item.removeStockNum(num); // 수량 감소
        return orderItem;
    }

    public int getTotalPrice() {
        int totalPrice = orderPrice * this.num;
        return totalPrice;//=orderPrice?
    }
    //주문 취소시 수량만큼 재고를 더해주는 로직
    public void cancel(){
        this.getItem().addStock(num);
    }


}

