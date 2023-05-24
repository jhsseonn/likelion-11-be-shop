package com.likelion.beshop.entity;

import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.constant.Role;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderItemRepository;
import com.likelion.beshop.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem() {
        Item item = new Item();
        item.setItemName("item_name");
        item.setPrice(100);
        item.setStockNumber(10);
        item.setDetail("item_detail");
        //item.setUploadTime(LocalDateTime.now());
        //item.setEditTime(LocalDateTime.now());
       // Item savedItem = itemRepository.save(item);

        return item;
    }


    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            Item savedItem = itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setPrice(1000);
            orderItem.setCount(10);
           // orderItem.setUploadTime(LocalDateTime.now());
           // orderItem.setEditTime(LocalDateTime.now());

            //orderRepository.saveAndFlush(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        //em.flush();
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(3,savedOrder.getOrderItems().size());

    }

    public Order createOrder(){
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            Item savedItem = itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setPrice(1000);
            orderItem.setCount(10);
           // orderItem.setUploadTime(LocalDateTime.now());
           // orderItem.setEditTime(LocalDateTime.now());

            //orderRepository.saveAndFlush(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member(); //멤버 필드 추가
        memberRepository.save(member);

        order.setMember(member); //위에 만들어준 멤버 객체 넣음
        orderRepository.save(order);

//        order.getMember().getId();
        order.setOrderStatus(OrderStatus.ORDERSUCCESS);
//        order.setUploadTime(LocalDateTime.now());
//        order.setEditTime(LocalDateTime.now());

        return order;
    }


    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest(){
       Order order = this.createOrder();
       order.getOrderItems().remove(0);
       em.flush();
    }
//
    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order = this.createOrder();
        Long orderItemId =order.getOrderItems().get(0).getId();

        em.flush();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("Order class : " + orderItem.getOrder().getClass());


    }
}