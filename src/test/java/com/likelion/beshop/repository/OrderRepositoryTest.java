package com.likelion.beshop.repository;

import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Order;
import com.likelion.beshop.entity.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.likelion.beshop.constant.ItemSellStatus.SALE;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    EntityManager em;

    public OrderItem createItem(Order order){
        Item miniitem = new Item();
        itemRepository.save(miniitem);

        OrderItem item=new OrderItem();
        item.setItem(miniitem);
        item.setCount(5);
        item.setPrice(1500);
        item.setUploadTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        item.setOrder(order);
        return item;
    }
    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){
        Order order = new Order();
        OrderItem item1=createItem(order);
        OrderItem item2=createItem(order);
        OrderItem item3=createItem(order);
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.add(item1);
        orderItems.add(item2);
        orderItems.add(item3);

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder()
    {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.ORDER);
        for (int i = 0;i<5;i++){
            OrderItem item = this.createItem(order);
            em.persist(item);
            order.getOrderItems().add(item);
        }
        Member member =new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest()
    {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest()
    {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();

        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class: "+orderItem.getOrder().getClass());
    }
}