package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId; // 장바구니 상품 아이디
    private List<CartOrderDto> cartOrderDtoList; // 장바구니 주문 상품 리스트

}