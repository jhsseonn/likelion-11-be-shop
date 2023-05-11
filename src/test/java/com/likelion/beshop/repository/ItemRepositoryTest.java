package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PersistenceContext;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalTime.now;
import static org.junit.jupiter.api.Assertions.*;




@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void saveItemTest(){

        Item item = new Item();
        item.setItemNm("노트북");
        item.setPrice(1000000);
        item.setStockNumber(10);
        item.setItemDetail("최신형노트북");
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품 10개 만들기")
    public void createItemList(){
        for(int i=0;i<10;i++) {
            Item item = new Item();
            item.setItemNm("상품"+i);
            item.setPrice(1000000-i);
            item.setStockNumber(10);
            item.setItemDetail("최신형노트북"+i);
            item.setItemSellStatus(ItemSellStatus.SALE);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
        }
    }


    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest(){
        this.createItemList();
        //Item 소문자이면 안됨
        List<Item> itemList = itemRepository.findByItemNm("상품1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명,상품 상세 설명 or 테스트")
    public void findByItemOrTest(){
        this.createItemList();
        //Item 소문자이면 안됨
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("상품3","최신형노트북1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByItemPriceLessThanTest(){
        this.createItemList();
        //Item 소문자이면 안됨
        List<Item> itemList = itemRepository.findByPriceLessThan(1000000);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByItemPriceLessOrderByTest(){
        this.createItemList();
        //Item 소문자이면 안됨
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(1000000);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemNameTest(){
        this.createItemList();
        //Item 소문자이면 안됨
        List<Item> itemList = itemRepository.findByItemDetail("최신형노트북1");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }


    //1번
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }
    //2번
//    @Bean
//    public JPAQueryFactory jpaQueryFactory() {
//        return new JPAQueryFactory(entityManager);
//    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.sellStatus.eq(ItemSellStatus.SALE))
                .where(qItem.detail.like("최신형노트북" + "%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println(item.toString());
        }

    }


}