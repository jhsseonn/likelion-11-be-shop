package com.likelion.beshop.repository;

import com.likelion.beshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long Id); // 상품 ID로 검색, 상품 이미지 ID 순으로 정렬하는 쿼리메소드 작성 (매개변수=상품ID)

    // 상품이미지 레포지토리에 상품 대표 이미지를 찾는 쿼리 메소드
    ItemImg findByIdAndRepImage(Long Id, String repImage); // 상품의 ID와 대표이미지여부(”Y”, N”)을 매개변수로 받아와 넘겨주는 쿼리 메소드
}
