package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderItemRepository;
import com.likelion.beshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    //아이템 생성 메소드 만들기

    @DisplayName("아이템 생성 메소드")
    public Item createItem(){
        Item item = new Item();
        item.setItemNm("노트북");
        item.setPrice(1000000);
        item.setStockNumber(10);
        item.setItemDetail("최신형노트북");
        item.setItemSellStatus(ItemSellStatus.SALE);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }


    @Test
    @DisplayName("영속성 전이 테스트 메소드")
    public void cascadeTest(){
        //Order 객체 만들고
        Order order = new Order();

        // 아이템 객체 3개 생성해서
        for(int i=0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);

            //주문 상품으로 추가하기
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(5);
            orderItem.setPrice(1000);
            orderItem.setRegTime(LocalDateTime.now());
            orderItem.setUpdateTime(LocalDateTime.now());
            orderItem.setOrders(order);

            order.getOrderItems().add(orderItem);

        }

        //orderRepository의 saveAndFlush 로 주문 객체 저장
        orderRepository.saveAndFlush(order);

        //em닫아주기
        em.clear();

        //orderRepository의 함수 이용해 savedOrder생성 (이때 예외처리 확실히)
        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("엔티티 없으면 오류"));
        assertEquals(3,savedOrder.getOrderItems().size());


    }

    //주문 생성 메소드 추가 - createOrder()
    //order객체의 member 필드는 어떻게 추가해야할 지 잘 생각해봅시다.

    @Autowired
    MemberRepository memberRepository;

    public Order createOrder(){
        Order order = new Order();
        for(int i=0;i<3;i++){
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(5);
            orderItem.setPrice(1000000);
            orderItem.setOrders(order);

            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }


    //order객체 추가
    //order객체의 orderItem 리스트의 0번째 인덱스 요소 제거 후 em에 flush
    @Test
    @DisplayName("고아객체 제가 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        Long orderItem_id = order.getOrderItems().get(0).getId();
        order.getOrderItems().remove(0);
        em.flush();
        assertEquals(Optional.empty(),orderRepository.findById(orderItem_id));

    }

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        //order객체 생성 및 orderItemId로 order객체의 orderItem리스트의 0번째 인덱스의 Id가져와 넣어주기
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();

        //em에 flush후 닫아주기
        em.flush();
        em.clear();

        //orderItemRepository 이용해 조회한 결과 orderItem 객체에 저장
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrders().getClass());
        System.out.println("==================================");
        orderItem.getOrders().getDate();


    }




}