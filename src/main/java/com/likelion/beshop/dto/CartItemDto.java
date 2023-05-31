package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long itemId; // 상품 아이디: Long (null값 허용x)

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int count; // 상품 수량: int (최소 수량: 1)

}
