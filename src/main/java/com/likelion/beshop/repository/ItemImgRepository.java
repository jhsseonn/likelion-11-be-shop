package com.likelion.beshop.repository;

import com.likelion.beshop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    // 매개변수로 넘겨준 상품 아이디를 가지고 , 상품 이미지 아이디의 오름차순으로 가져옴
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);//쿼리 메소드



    //상품의 ID와 대표이미지 여부(Y/N)을 매개변수로 받아와 넘겨주는 쿼리메소드
    //해당 상품의 대표이미지를 넘겨줌
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}