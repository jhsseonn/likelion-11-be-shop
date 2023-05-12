package com.likelion.beshop.repository;

import com.likelion.beshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByIdOrderByIdAsc(Long Id); // 상품 ID로 검색, 상품 이미지 ID 순으로 정렬하는 쿼리메소드 작성 (매개변수=상품ID)
}
