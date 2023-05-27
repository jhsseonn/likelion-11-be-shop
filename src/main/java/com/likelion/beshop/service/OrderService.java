package com.likelion.beshop.service;

import com.likelion.beshop.dto_.OrderDto;
import com.likelion.beshop.dto_.OrderHistDto;
import com.likelion.beshop.dto_.OrderItemDto;
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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

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

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        List<Order> orders = orderRepository.findOrders(email, pageable); //회원 이메일과 페이징 조건으로 주문 목록 조회
        Long totalCount = orderRepository.countOrder(email); //이메일로 주문 횟수 조회

        List<OrderHistDto> orderHistDtos = new ArrayList<>(); //주문을 받아올 리스트 생성

        for(Order order : orders) //주문 리스트를 돌면서
    {
        OrderHistDto orderHistDto = new OrderHistDto(order); //페이지에 전달할 이력 DTO 생성
        List<OrderItem> orderItems = order.getOrderItems(); //주문의 상품 리스트를 리스트로 받아오기
        for (OrderItem orderItem : orderItems) { //받아온 상품들의 이미지 조회
            ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
            OrderItemDto orderItemDto = //주문 상품 DTO 생성
                    new OrderItemDto(orderItem, itemImg.getImgUrl());
            orderHistDto.addOrderItemDto(orderItemDto); //해당 DTO 주문 이력 DTO의 주문 상품에 추가
        }
        orderHistDtos.add(orderHistDto); //주문 이력 DTO를 주문 내역 리스트에 추가
    }
        return new PageImpl<OrderHistDto>(orderHistDtos,pageable,totalCount); //반환값은 pageImpl을 이용해 페이지 구현 객체를 생성한 값
    }

    @Transactional(readOnly = true) //주문한 사용자 = 현재 로그인한 사용자인지 확인하는 메소드
    public boolean validateOrder(Long orderId, String email){
        Member member = memberRepository.findByEmail(email); //이메일로 사용자 조회 로그인한 유저
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new); //주문 번호 조회
        Member savedMember = order.getMember(); //주문한 사용자 조회 주문한 유저

        if(!StringUtils.equals(member.getEmail(),savedMember.getEmail())){
            return false; //사용자의 이메일과 주문한 사용자의 이메일이 같은지 확인하고 이에 따라 참/거짓 반환
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new); //파라미터로 주문 번호 받아오기
        order.cancelOrder(); //해당 주문 취소 메소드 호출 -> 주문 취소 상태로 변경시, 변경 감지 기능에 의해 트랜잭션 끝날 때 update 쿼리 실행
    }

    public Long orders(List<OrderDto> orderDtoList, String email){
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for(OrderDto orderDto : orderDtoList){
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);

            OrderItem orderItem = OrderItem.createOrderItem(item,orderDto.getCount());
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member,orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
