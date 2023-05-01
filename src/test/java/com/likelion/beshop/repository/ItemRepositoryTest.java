package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemList1() {
        Item item = new Item();
        item.setName("이름");
        item.setPrice(1000);
        item.setNum(1);
        item.setContent("설명");

        item.setTime(LocalDateTime.now());
        item.setEditTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList() {
        for (int i = 1; i < 11; i++) {
            Item item = new Item();
            item.setName("이름"+i);
            item.setPrice(1000+i);
            item.setNum(1);
            item.setContent("설명"+i);

            item.setTime(LocalDateTime.now());
            item.setEditTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
            //System.out.println(savedItem.toString());
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByName("이름1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByPriceLessThan(1002);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNameTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByNameOrContent("이름1", "설명2");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }


    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDescTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByPriceLessThanOrderByPriceDesc(1005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }
}