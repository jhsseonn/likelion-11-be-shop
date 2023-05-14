package com.likelion.beshop.dto;
//ItemSearchDto는 상품 데이터 조회 시 상품 조회 조건을 갖고 있는 클래스
import com.likelion.beshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    //현재 시간과 상품 등록일을 비교해 조회
    private String searchDateType;
    //상품 판매 상태로 조회
    private ItemSellStatus searchSellStatus;
    //상품을 조회할 때 어떤 유형으로 조회할지
    private String searchBy;
    //조회할 검색어(쿼리)를 저장할 변수
    private String searchQuery = "";
}
