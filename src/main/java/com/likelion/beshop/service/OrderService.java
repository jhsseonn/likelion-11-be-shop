package com.likelion.beshop.service;

import com.likelion.beshop.dto_.OrderDto;
import com.likelion.beshop.entity.Item;
import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Order;
import com.likelion.beshop.entity.OrderItem;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityExistsException::new); //주문할 상품 조회
        Member member = memberRepository.findByEmail(email); //현재 회원의 이메일로 회원 정보 조회

        List<OrderItem> orderItemList = new ArrayList<>(); //주문 상품 리스트 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount()); //주문할 상품 엔티티와 주문 수량으로 주문 상품 생성
        orderItemList.add(orderItem); //생성한 주문 상품을 주문 상품 리스트에 추가

        Order order = Order.createOrder(member,orderItemList); //회원 정보와 주문 상품 리스트 정보로 주문 생성
        orderRepository.save(order); //생성한 주문 엔티티 저장

        return order.getId(); //셍성한 주문 아이디 리턴
    }
}
