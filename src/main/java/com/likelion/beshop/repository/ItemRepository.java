package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByName(String name);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    List<Item> findByNameOrContent(String name, String content);


}
