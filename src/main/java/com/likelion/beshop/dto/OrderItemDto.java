package com.likelion.beshop.dto;

import com.likelion.beshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imagePath) {
        this.itemName = orderItem.getItem().getName();
        this.count= orderItem.getCount();
        this.price = orderItem.getPrice();
        this.imagePath = imagePath;
    }

    //상품명
    private String itemName;

    //주문 수량
    private int count;

    //주문 금액
    private int price;

    //상품 이미지 경로
    private String imagePath;
}