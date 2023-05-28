package com.likelion.beshop.dto;

import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//데이터를 불러오고 세팅하는 데에 사용하는 어노테이션
@Getter
@Setter
public class OrderHistDto {

    // Order 객체를 파라미터로 받아서 멤버 변수값을 세팅
    public OrderHistDto(Order order){
        this.orderId = order.getCode();
        this.orderDate = order.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getStatus();
    }

    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문 상태

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    //주문 상품리스트, OrderItemDto를 매개변수로 받아와서 주문 상품 리스트에 추가해주는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}
