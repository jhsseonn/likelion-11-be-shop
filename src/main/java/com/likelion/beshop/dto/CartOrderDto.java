package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//장바구니 페이지에서 주문할 상품 데이터를 전달할 DTO
public class CartOrderDto {

    private Long cartItemId; // 장바구니 상품 아이디

    private List<CartOrderDto> cartOrderDtoList; // 장바구니 주문 상품 리스트
}
