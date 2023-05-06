package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("아이템 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setName("아이템");
        item.setPrice(1000);
        item.setNum(5);
        item.setDetail("생필품입니다.");
        item.setRegisterTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item saveItem = itemRepository.save(item);
        System.out.println(saveItem.toString());
    }

    public void createItem() {
        for (int i=0; i<10; i++){
            Item item = new Item();
            item.setName("아이템" + i);
            item.setDetail("생필품입니다" +i);
            item.setPrice(i*1000);
            item.setNum(5);
            item.setRegisterTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item saveItem = itemRepository.save(item);

            item.setStatus(ItemSellStatus.SELL);

        }
    }
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest() {
        this.createItem();
        List<Item> itemList = itemRepository.findByName("아이템1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명 혹은 상세설명 조회 테스트")
    public void findByItemNameOrDetailTest() {
        this.createItem();
        List<Item> itemList = itemRepository.findByNameOrDetail("아이템1", "생필품입니다3");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품가격 조회 테스트")
    public void findByItemPriceTest() {
        this.createItem();
        List<Item> itemList = itemRepository.findByPriceLessThan(5000);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품가격 조회 내림차순 테스트")
    public void findByItemPriceDescTest() {
        this.createItem();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(5000);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query를 이용한 상품 조회 테스트")
    public void findByDetailPriceDescTest() {
        this.createItem();
        List<Item> itemList = itemRepository.findByDetail("생필품입니다");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItem();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.status.eq(ItemSellStatus.SELL))
                .where(qItem.detail.like("%" + "생필품입니다" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for(Item item : itemList) {
            System.out.println(item.toString());

        }
    }
}

