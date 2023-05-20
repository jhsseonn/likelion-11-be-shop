package com.likelion.beshop.dto;

import com.likelion.beshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType; // 현재 시간과 상품 등록일 비교
    private ItemSellStatus searchSellStatus; // 상품 판매 상태로 조회
    private String searchBy; // 상품 조회시 어떤 것으로 조회할지
    private String searchQuery = ""; // 조회할 검색어 쿼리를 저장할 변수
}
