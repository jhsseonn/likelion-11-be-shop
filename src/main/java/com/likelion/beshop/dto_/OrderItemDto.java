package com.likelion.beshop.dto_;

import com.likelion.beshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemnm = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getPrice();
        this.imgUrl = imgUrl;
    }

    //상품명
    private String itemnm;

    //주문 수량
    private int count;

    //주문 금액
    private int orderPrice;
    //상품 이미지 경로

    private String imgUrl;
}