package com.likelion.beshop.repository;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.likelion.beshop.constant.ItemSellStatus.SALE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    ItemRepository itemRepository;

    public void saveTenItem(){
        for (var i=0;i<10;i++){
            Item item = new Item();
            Item temp;

            if (i % 2 == 0) {
                item.setName("허니버터칩");
                item.setDescription("맛있는 감자칩입니다.");
            }
            else{
                item.setName("포카칩");
                item.setDescription("그냥 그런 감자칩입니다.");
            }
            item.setPrice((i+1)*1000);
            item.setItemSellStatus(ItemSellStatus.SALE);
            item.setCount(i+2);
            temp=itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void saveItemTest()
    {
        Item item = new Item();
        Item temp;

        item.setName("허니버터칩");
        item.setPrice(1300);
        item.setItemSellStatus(ItemSellStatus.SALE);
        item.setDescription("맛있는 감자칩입니다.");
        item.setCount(5);
        temp=itemRepository.save(item);
        System.out.println(temp.toString());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNumTest(){
        this.saveTenItem();
        List<Item> itemList = itemRepository.findByName("허니버터칩");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByNameOrDescriptionTest(){
        this.saveTenItem();
        List<Item> itemList = itemRepository.findByNameOrDescription("허니버터칩", "맛있는 감자칩입니다.");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByLessThanPriceTest(){
        this.saveTenItem();
        List<Item> itemList = itemRepository.findByPriceLessThan(3000);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByLessThanOrderByTest(){
        this.saveTenItem();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(3000);
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query이용한 상품 조회 테스트")
    public void querySelectTest(){
        this.saveTenItem();
        List<Item> itemList = itemRepository.findByDescription("맛있는 감자칩입니다.");
        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("QueryDSL 조회 테스트1")
    public void queryDslTest(){
        this.saveTenItem();
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(SALE))
                .where(qItem.description.like("맛있는 감자칩입니다."))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
}