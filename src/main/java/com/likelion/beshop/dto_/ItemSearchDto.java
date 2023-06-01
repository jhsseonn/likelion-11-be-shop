package com.likelion.beshop.dto_;

import com.likelion.beshop.constant.ItemSellStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ItemSearchDto {

    private String searchDateType;
    //현재 시간과 상품 등록일 비교해 상품 데이터 조회하는 필드, 조회시간 기준 - all(상품 등록일 전체), 1d(최근 하루)

    private ItemSellStatus searchSellStatus;
    //상품 판매 상태를 기준으로 상품 데이터를 조회하는 필드

    private String searchBy;
    //상품을 조회할 때 어떤 유형으로 조회할지 선택해 조회하는 필드

    private String searchQuery = "";
    //조회할 검색어 저장할 변수
}