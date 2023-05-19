package com.likelion.beshop.service;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.dto.OrderHistDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Order;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderRepository;
import com.likelion.beshop.service.ItemImgService;
import com.likelion.beshop.service.ItemService;
import com.likelion.beshop.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.likelion.beshop.constant.ItemSellStatus.SELLING;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    com.likelion.beshop.repository.ItemRepository itemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderService orderService;
    public Item saveItem(){
        Item item = new Item();
        item.setName("아이템");
        item.setPrice(11);
        item.setDetail("12");
        item.setStockNum(13);
        item.setSellStatus(ItemSellStatus.SELLING);
        return itemRepository.save(item);
    }
    public Member saveMember(){
        Member member = new Member();
        member.setEmail("seoyoon@naver.com");
        return memberRepository.save(member);
    }
    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item= saveItem();
        Member member= saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        int totalPrice = orderDto.getCount() * item.getPrice();
        assertEquals(totalPrice, order.getTotalPrice());

    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setItemId(item.getId());
        orderDto.setCount(1);
        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCEL,order.getOrderStatus());
        assertEquals(13,item.getStockNum());
    }

}
