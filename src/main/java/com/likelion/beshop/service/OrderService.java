package com.likelion.beshop.service;

import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Order;
import com.likelion.beshop.entity.OrderItem;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long order(OrderDto orderDto, String email){ // 파라미터로 OrderDto랑 email

        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);    // 주문 상품 조회
        Member member = memberRepository.findByEmail(email); //회원 정보 조회

        List<OrderItem> orderItemList = new ArrayList<>();  // 주문 상품 리스트 생성 
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); // 주문 상품 엔티티와 주문 수량으로 주문 상품 생성
        orderItemList.add(orderItem);   // 주문 상품 리스트에 추가

        Order order = Order.createOrder(member, orderItemList); // 회원 정보와 주문 상품 리스트 정보로 주문 생성
        orderRepository.save(order);    // 생성한 주문 엔티티 저장

        return order.getId();
    }


}
