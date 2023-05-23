package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {
    private Long cartItemId;
    private String itemName;
    private int price;
    private int count;
    private String imgPath;

    // 생성자
    public CartDetailDto(Long cartItemId, String itemName, int price, int count, String imgPath) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imgPath = imgPath;
    }
}
