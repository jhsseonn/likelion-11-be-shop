package com.likelion.beshop.dto;

import com.likelion.beshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    //생성자
    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.name = orderItem.getItem().getName();// 상품명
        this.count = orderItem.getNum(); // 주문 수량
        this.orderPrice = orderItem.getOrderPrice();// 주문 금액
        this.imgUrl = imgUrl; // 상품 이미지 경로
    }
    private String name;
    private int count;
    private int orderPrice;
    private String imgUrl;
}
