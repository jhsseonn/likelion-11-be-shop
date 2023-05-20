package com.likelion.beshop.repository;

import com.likelion.beshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    // 상품 Id를 기준으로 오름차순 정렬한 ItemImg 리스트
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    // 상품 아이디와 대표 이미지 여부(Y, N)로 아이템 대표 이미지 조회
    ItemImg findByItemIdAndRepImg(Long itemId, String repImg);

}
