package com.likelion.beshop.service;

import com.likelion.beshop.dto.OrderDto;
import com.likelion.beshop.dto.OrderHistDto;
import com.likelion.beshop.dto.OrderItemDto;
import com.likelion.beshop.entity.*;
import com.likelion.beshop.repository.ItemImgRepository;
import com.likelion.beshop.repository.ItemRepository;
import com.likelion.beshop.repository.MemberRepository;
import com.likelion.beshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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

    private final ItemImgRepository itemImgRepository;
    private final MemberService memberService;

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

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 회원 이메일과 페이징 조건으로 주문 목록 조회
        List<Order> orders = orderRepository.findOrders(email, pageable);
        // 이메일로 주문 횟수 조회
        Long totalCount = orderRepository.countOrder(email);

        // 주문 내역을 받아올 리스트
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            // 주문 리스트 돌면서 페이지에 전달할 이력 DTO 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);
            // 주문상품 리스트를 리스트로 받아오기
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                //받아온 상품들의 대표 이미지 조회
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                // 주문 상품 DTO 생성
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                // 해당 DTO 주문 이력 DTO의 주문 상품에 추가
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            // 주문 이력 DTO를 주문 내역 리스트에 추가
            orderHistDtos.add(orderHistDto);
        }
        // PageImpl로 페이지 구현 객체 생성한 값 반환
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member member = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(member.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }


}
