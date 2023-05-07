package com.likelion.entity;

import com.likelion.beshop.BeShopApplication;
import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Order;
import com.likelion.beshop.entity.OrderItem;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = BeShopApplication.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderTest {

    @Autowired
    ItemRepository itemRepository;

   @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setName("이름");
        item.setPrice(1000);
        item.setNum(1);
        item.setContent("설명");
        item.setStatus(ItemSellStatus.SELLING);

        item.setTime(LocalDateTime.now());
        item.setEditTime(LocalDateTime.now());

        return  item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setNum(3);
            orderItem.setPrice(234);
            orderItem.setTime(LocalDateTime.now());
            orderItem.setEditTime(LocalDateTime.now());
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);

        em.clear();


        Order savedOrder = orderRepository.findById(order.getCode())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(3, savedOrder.getOrderItems().size());
    }
}
