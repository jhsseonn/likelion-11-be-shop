package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {

    private Long cartItemId; // 장바구니에 담은 상품 아이디: Long

    private String itemNm; // 상품 이름: String

    private int price; // 상품 가격: int

    private int count; // 상품 수량: int

    private String imgUrl; // 상품 이미지 경로: String

    // 객체 생성자 함수
    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }

}