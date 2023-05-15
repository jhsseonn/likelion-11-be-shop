package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    List<Item> findByName(String name);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    List<Item> findByNameOrContent(String name, String content);
    @Query("select i from Item i where i.content like %:content% order by i.price desc")
    List<Item> findByContent(@Param("content") String content);

}
