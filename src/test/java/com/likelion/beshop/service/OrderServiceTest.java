package com.likelion.beshop.service;

import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.dto.OrderDto;
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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static com.likelion.beshop.constant.ItemSellStatus.SELLING;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setName("테스트 상품");
        item.setPrice(10000);
        item.setContent("테스트 상품 상세 설명");
        item.setStatus(SELLING);
        item.setNum(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);

    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        int totalPrice = orderDto.getCount()*item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void cancelOrder(){
        // 상품과 회원 저장 메소드 호출
        Item item = saveItem();
        Member member = saveMember();

        // OrderDto에 주문할 상품, 수량 세팅
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());
        Long orderId = orderService.order(orderDto, member.getEmail()); // 주문 로직 호출 결과로 생성된 주문 번호를 orderId라는 변수에 저장

        // 주문 번호로 저장된 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        orderService.cancelOrder(orderId); // 해당 주문 취소

        assertEquals(OrderStatus.UNDO, order.getStatus()); // assertEquals 사용하여 주문 상태 '취소'와 주문의 주문 상태 비교
        assertEquals(100, item.getNum()); // assertEquals 사용하여 saveItem에서 지정해준 재고수와 취소 후 상품 재고수 비교
    }

}
