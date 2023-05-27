package com.likelion.beshop.dto_;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {

    private  Long cartItemId;
    private String itemNm;
    private int price;
    private int count;
    private String imgUrl;

    //Dto 객체로 변환 후 반환하기 위한 생성자
    public CartDetailDto(Long cartItemId, String itemNm, int price, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
