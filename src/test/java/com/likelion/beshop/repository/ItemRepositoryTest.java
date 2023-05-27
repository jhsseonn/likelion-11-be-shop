package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
//import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.likelion.beshop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

//import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {


    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("상품저장테스트")
    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("item_name" + i);
            item.setPrice(i);
            item.setStockNumber(10);
            item.setDetail("item_detail" + i);
           // item.setUploadTime(LocalDateTime.now());
           // item.setEditTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
            // System.out.println((savedItem.toString()));
        }
    }
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemName("item_name1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명 상품 상세 일치 테스트")
    public void findByItemNameOrDetail(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNameOrDetail("item_name1","item_detail1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격이 낮은 상품 찾기")
    public void findByPriceLessThan() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(8);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림 차순")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(9);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품 상세 설명 조회 후 높은순")
    public void findByItemDetail(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("item_detail");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem) //JPAQueryFactory를 이용하여 JPAQuery<Item> 객체를 생성, 생성된 JPAQuery 객체를 통해 QItem 객체를 이용하여 쿼리를 동적으로 생성
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL)) //생성된 쿼리는 ItemSellStatus가 SALE이고, detail에 "상세 설명"이 포함되며, price를 기준으로 내림차순으로 정렬합니다.
                .where(qItem.detail.like("%" + "상세 설명" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch(); //fetch를 이용하여 쿼리 결과를 리스트로 반환

        for(Item item : itemList) {
            System.out.println(item.toString());

        }
    }
}