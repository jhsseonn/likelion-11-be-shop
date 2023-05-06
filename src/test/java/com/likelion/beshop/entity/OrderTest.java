package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderItemRepository;
import com.likelion.beshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    // 아이템 생성 메소드
    public Item createItem() {
        Item item = new Item();
        item.setName("아이템");
        item.setPrice(1000);
        item.setNum(1);
        item.setDetail("설명");
        item.setStatus(ItemSellStatus.SOLD_OUT);
        item.setRegisterTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;

    }

    @Test
    @DisplayName("영속성 전이 테스트") //주문 저장할 때 주문 상품으로 저장한 상품 3개가 영속성 전이되면서 주문 상품으로 저장됨. 즉 Item이 3번 insert, order가 (1번)insert된 이후 orderItem이 3번 insert됨
    public void cascadeTest() {
        // Order 객체 생성
        Order order = new Order();

        // 아이템 생성 (3개) 해서 주문 상품으로 추가하기
        for(int i=0; i < 3; i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(5);
            orderItem.setPrice(1000);
            //orderItem.setRegisterTime(LocalDateTime.now());
            //orderItem.setUpdateTime(LocalDateTime.now());
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
        }


        orderRepository.saveAndFlush(order); // 주문 객체 저장
        em.clear(); // em 닫아주기

        Order savedOrder = orderRepository.findById(order.getId()) //orderRepository 함수로 savedOrder 생성
                .orElseThrow(EntityNotFoundException::new); // 예외 처리
        assertEquals(3, savedOrder.getOrderItems().size()); // assertEquals로 savedOrder의 주문 상품 리스트 사이즈가 3과 같은지 확인

    }

    // 주문 생성 메소드
    public Order createOrder() {
        //Order 객체 생성
        Order order = new Order();

        // 아이템 생성 (3개) 해서 주문 상품으로 추가하기
        for(int i=0; i < 3; i++){
            Item item = this.createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(5);
            orderItem.setPrice(1000);
            //orderItem.setRegisterTime(LocalDateTime.now());
            //orderItem.setUpdateTime(LocalDateTime.now());
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
        }

        // member 필드 추가
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("고아 객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();   // order 객체 추가
        order.getOrderItems().remove(0); // order 객체의 orderItem 리스트의 0번째 인덱스 요소 제거
        em.flush(); // em에 flush
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();    // order 객체 생성
        Long orderItemId = order.getOrderItems().get(0).getId();// order 객체의 orderItem 리스트의 0번째 인덱스의 Id를 넣어주기

        em.flush(); // DB에 반영
        em.clear(); // 초기화

        // orderItemRepositroy로 조회한 결과 orderItem 객체에 저장
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                        .orElseThrow(EntityNotFoundException::new); //예외처리

        System.out.println("Order class: "+ orderItem.getOrder().getClass());//orderItem 객체의 클래스 프린트해보기
    }


}