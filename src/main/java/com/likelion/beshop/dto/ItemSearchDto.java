package com.likelion.beshop.dto;

import com.likelion.beshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto{
    //현재 시간과 상품 등록일을 비교해 조회
    // all : 상품 등록 전체
    // 1d : 최근 하루 동안 등록된 상품
    // 1w : 최근 일주일 동안 등록된 상품
    // 1m : 최근 한 달 동안 등록된 상품
    // 6m : 최근 6개월 동안 등록된 상품
    private String searchDateType;
    //상품 판매 상태로 조회
    private ItemSellStatus searchSellStatus;
    //상품을 조회할 때 어떤 유형으로 조회할지
    private String searchBy;
    //조회할 검색어(쿼리)를 저장할 변수
    private String searchQuery = "";

}
