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
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Long order(OrderDto orderDto, String email) {

        // 주문할 상품 조회
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 이메일로 회원 정보 조회
        Member member = memberRepository.findByEmail(email);

        // 주문 상품 리스트 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());

        // 생성한 주문 상품 리스트에 추가
        orderItemList.add(orderItem);

        // 주문 생성
        Order order = Order.createOrder(member, orderItemList);

        // 주문 엔티티 저장
        orderRepository.save(order);

        // 주문 번호 반환
        return order.getId();
    }
}
