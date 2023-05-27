package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CartItemDto {

    @NotNull(message="상품 아이디는 필수 입력 값입니다.")
    private Long itemId;

    @Min(value=1, message="최소 장바구니 수량은 1개입니다.")
    private int count;

}
