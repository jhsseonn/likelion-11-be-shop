package com.likelion.beshop.service;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto_.OrderDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Order;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

//    private final OrderService orderService;

    @Autowired
    private OrderService orderService;

    @Autowired
   private OrderRepository orderRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemName("바나나콘칩");
        item.setPrice(1000);
        item.setDetail("바나나");
        item.setStockNumber(10);
        item.setItemSellStatus(ItemSellStatus.SELL);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("amu6675@naver.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember(); //위에서 저장한 아이템, 멤버 가져옴

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId()); //상품 상세 페이지 화면에서 넘어오는 값들 설정

        Long orderId = orderService.order(orderDto,member.getEmail()); //주문 객체 DB에 저장
        Order order = orderRepository.findById(orderId) //1. 저장된 주문 객체 조회
                .orElseThrow(EntityNotFoundException::new);


        int totalPrice = orderDto.getCount()* item.getPrice(); //2.위에서 만든 주문 상품 총 가격

        assertEquals(totalPrice,order.getTotalPrice());
    }
}