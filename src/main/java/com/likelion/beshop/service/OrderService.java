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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository;

    // 각 상품 페이지에서 바로 주문하는 로직
    public Long order(OrderDto orderDto, String email) {

        // 주문할 상품 조회
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

        // 이메일로 회원 정보 조회
        Member member = memberRepository.findByEmail(email);
        System.out.println(member.getName());

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

    // 주문 조회 로직
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        // 회원 이메일과 페이징 조건으로 주문 목록 리스트 조회 (한 회원의 주문 내역)
        List<Order> orders = orderRepository.findOrders(email, pageable);
        // 회원 이메일로 총 주문 횟수 조회
        Long totalCount = orderRepository.countOrder(email);
        // 주문 내역을 저장할 리스트
        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
            // 주문 하나에 존재하는 상품들을 주문 상품 리스트에 넣기
            List<OrderItem> orderItems = order.getOrderItems();

            // 주문 이력 Dto 선언
            OrderHistDto orderHistDto = new OrderHistDto(order);

            // 해당 주문 상품 리스트에 있는 각 상품의 대표 이미지 조회
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImg(orderItem.getItem().getId(), "Y");

                // 해당 주문 상품과 대표 이미지 경로로 주문 상품 Dto 생성
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgPath());

                // 주문 상품 Dto를 주문 이력 Dto 내부의 주문 상품 Dto 리스트에 추가
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            // 주문 이력 Dto 리스트에 주문 내역 Dto 추가
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    // 주문한 사용자가 현재 로그인한 사용자인지 확인하는 메소드
    @Transactional(readOnly = true)
    public boolean validateOrder(long orderId, String email) {
        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(email);

        // 주문 번호로 주문 조회
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        // 주문한 사용자 조회
        Member savedMember = order.getMember();

        // 로그인한 사용자와 주문한 사용자의 이메일이 같은지 비교
        if (!StringUtils.equals(member.getName(), savedMember.getName())) {
            return false;
        }
        return true;
    }

    // 주문 취소
    public void cancelOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    // 장바구니 상품 주문
    public Long orders(List<OrderDto> orderDtoList, String email) {
        // 이메일로 사용자 조회
        Member member = memberRepository.findByEmail(email);

        // 주문 완료된 상품을 저장할 리스트 생성
        List<OrderItem> orderItemList = new ArrayList<>();

        // 주문 dto 돌며
        for (OrderDto orderDto : orderDtoList) {
            // 주문 dto의 상품 id를 통해 상품 조회
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            // 조회한 상품과 주문 페이지의 수량을 통해 주문 상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            // 주문 상품 리스트에 주문 상품 추가
            orderItemList.add(orderItem);
        }

        // 멤버와 주문한 상품 리스트를 통해 주문 생성
        Order order = Order.createOrder(member, orderItemList);
        // db에 저장
        orderRepository.save(order);

        // 주문 id 리턴
        return order.getId();
    }
}
