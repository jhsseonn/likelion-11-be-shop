package com.likelion.beshop.repository;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.entity.Cart;
import com.likelion.beshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 특정 상품이 카트에 존재하는지 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    //
    @Query("select new com.likelion.beshop.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgPath) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.repImg = 'Y' " +
            "and ci.item.id = im.item.id " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
