package com.likelion.beshop.repository;

import com.likelion.beshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg,Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId); //상품 아이디로 검색, 상품 이미지 아이디 수느올 정렬하는 쿼리 메소드
}
