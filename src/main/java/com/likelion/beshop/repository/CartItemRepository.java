package com.likelion.beshop.repository;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartCodeAndItemId(Long cartId, Long itemId); // 장바구니 아이디와 상품 아이디로 장바구니에 담은 상품 조회

    // 주의: CartDetailDto 생성자를 이용해 Dto를 반환할 때는 new 키워드와 해당 DTO의 패키지, 클래스명을 적어줌.
    // 또, 파라미터 순서는 DTO 클래스에 명시한 순으로 넣어줌
    @Query("select new com.likelion.beshop.dto.CartDetailDto(ci.code, i.name, i.price, ci.num, im.imagePath) " +
            "from CartItem ci, ItemImg im " + // from : CartItem을 ci로, ItemImg를 im으로 가져오기
            "join ci.item i " + // join : ci.item을 i로 해 조인
            "where ci.cart.code = :cartId " + // where : ci.cart.code에서 cartId와 같고(이 때, 이름 기준 파라미터 바인딩 - 변수명 앞에 : 붙여줌)

            "and im.item.id = ci.item.id " +  // im.item.id는 ci.item.id와 같고,
            "and im.repImage = 'Y' " + // im.repImage은 값이 'Y'인 데이터 조회
            "order by ci.regTime desc" // order by 사용해 ci.regTime 내림차순으로 정렬
    )

    List<CartDetailDto> findCartDetailDtoList(Long cartId); // cartId 파라미터로 받아 CartDetailDtoList 검색하는 메서드
}
