package com.likelion.beshop.dto;

import com.likelion.beshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

// 주문한 데이터를 조회하는 Dto
@Getter
@Setter
public class OrderItemDto {

    // 주문한 상품 객체의 정보를 Dto에 담음
    public OrderItemDto(OrderItem orderItem, String imgPath) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgPath = imgPath;
    }

    private String itemName;
    private int count;
    private int orderPrice;
    private String imgPath;
}
