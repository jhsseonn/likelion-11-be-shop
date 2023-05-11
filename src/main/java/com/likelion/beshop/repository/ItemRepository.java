package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemNm(String itemNm);
    //1번
    List<Item> findByItemNmOrItemDetail(String ItemNm, String itemDetail);

    //2번
    List<Item> findByPriceLessThan(Integer price);

    //3번
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //Item의 별칭 i
    @Query("select i from Item i where i.itemDetail like %:detail% order by i.price desc")
    List<Item> findByItemDetail(@Param("detail") String detail);



}
