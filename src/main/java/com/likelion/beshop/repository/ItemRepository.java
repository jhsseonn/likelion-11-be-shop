package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    List<Item> findByName(String name);
    List<Item> findByNameOrDetail(String name,String detail);
    List<Item> findByPriceLessThan(int price);
    List<Item> findByPriceLessThanOrderByPriceDesc(int price);
    @Query("SELECT e FROM Item e WHERE e.detail LIKE %:detail% ORDER BY e.price desc")
    List<Item> findByDetail(@Param("detail") String detail);



}
