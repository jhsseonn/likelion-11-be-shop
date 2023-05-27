package com.likelion.beshop.repository;

import com.likelion.beshop.dto_.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}



//상품 조회 조건을 담고 있는 itemSearchDto 객체와 페이징 정보를 담고 있는
//pageable 객체를 파라미터로 받고, Page<Item> 객체를 반환