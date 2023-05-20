package com.likelion.beshop.dto;

import com.likelion.beshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

//주문한 데이터를 조회하기 위해 데이터를 받아올 DTO클래스
@Getter
@Setter
public class OrderItemDto {

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getPrice();
        this.imgUrl = imgUrl;
    }

    //상품명
    private String itemNm;
    //주문 수량
    private int count;
    //주문 금액
    private int orderPrice;
    //상품 이미지 경로
    private String imgUrl;


}
