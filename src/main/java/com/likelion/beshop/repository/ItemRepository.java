package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByName(String name);

    // 상품명 혹은 상세 설명 중 어느 하나가 일치할 때 아이템을 가져오는 쿼리 메소드
    List <Item> findByNameOrDetail(String name, String detail);

    // 특정 가격보다 값이 작은 상품들을 조회하는 쿼리 메서드
    List<Item> findByPriceLessThan(Integer price);
    // 특정 가격보다 값이 작은 상품들을 조회한 결과를 가격이 비싼 순대로 정렬한 쿼리 메소드
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("SELECT i FROM Item i where i.detail like %:detail% order by i.price desc")
    List<Item> findByDetail(@Param("detail") String detail);
}
