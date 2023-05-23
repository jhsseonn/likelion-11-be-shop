package com.likelion.beshop.repository;

import com.likelion.beshop.dto.CartDetailDto;
import com.likelion.beshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
    @Query("SELECT new com.likelion.beshop.dto.CartDetailDto(ci.id, i.name, i.price, ci.count, im.imagePath) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.imageMainYN = 'Y' " +
            "and im.item.id = ci.item.id " +
            "order by ci.regTime desc")
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId")Long cartId);
}
