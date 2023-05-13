package com.likelion.beshop.repository;

import com.likelion.beshop.dto.ItemSearchDto;
import com.likelion.beshop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
