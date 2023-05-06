package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
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

import static com.querydsl.core.types.ExpressionUtils.orderBy;

@Transactional
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager manager;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setName("나나");
        item.setNum(1);
        item.setDetail("ss");
        item.setPrice(123);
        item.setSellStatus(ItemSellStatus.SELLING);
        item.setRegisterTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByName("나나2");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }


    }

    public void createItemList() {
        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setName("나나" + i);
            item.setNum(1);
            item.setDetail("ss");
            item.setPrice(100*i);
            item.setSellStatus(ItemSellStatus.SELLING);
            item.setRegisterTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);

            item.setSellStatus(ItemSellStatus.SELLING);

        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void ProductNameOrDetailTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByNameOrDetail("나나2", "ss");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("가격LessThan테스트")
    public void PriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByPriceLessThan(124);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격LessThan테스트")
    public void PriceLessThanDescTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByPriceLessThanOrderByPriceDesc(124);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }
    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void QueryTest() {
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByDetail("ss");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(manager);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.sellStatus.eq(ItemSellStatus.SELLING))
                .where(qItem.detail.like("%" + "s" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }


}



