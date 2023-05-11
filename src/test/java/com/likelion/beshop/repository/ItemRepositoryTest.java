package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.likelion.beshop.constant.ItemSellStatus.SELL;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setStock(100);
        item.setItemDetail("테스트 상품 상세 설명");
        Item saveItem = itemRepository.save(item);
        System.out.println(saveItem.toString());
    }

    public void createItemList() {

        for (int i = 0; i < 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + (i*1000));
            item.setStock(100);
            item.setItemDetail("테스트 상품상세설명" + (i+1));
            Item saveItem = itemRepository.save(item);
        }
    }
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNameTest() {

        // 아이템 생성
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemName("테스트 상품1");

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNameOrDetailTest() {
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("테스트 상품1", "테스트 상품상세설명1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThan() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByPriceLessThan(12000);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(20000);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetail() {

        this.createItemList();

        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품상세설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회 테스트 1")
    public void queryDslTest() {
        this.createItemList();
        // 쿼리를 동적으로 생성, 생성자 파라미터에 EntityManager 객체 넣어줌
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // 자동으로 생성된 QItem 객체 사용
        QItem qItem = QItem.item;
        // SQL문과 비슷하게 소스코드로 작성
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(SELL)) // 판매중인 상품 중
                .where(qItem.itemDetail.like("%" + "테스트 상품상세설명" + "%")) // 상세 설명에 해당 문구가 들어가는 상품을
                .orderBy(qItem.price.desc()); // 가격이 높은순으로 내림차순 정렬
        List<Item> itemList = query.fetch(); // 리스트로 결과를 받아옴

        for (Item item : itemList)
            System.out.println(item.toString());
    }
}
