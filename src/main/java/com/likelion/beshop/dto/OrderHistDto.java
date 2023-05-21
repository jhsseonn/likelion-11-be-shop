package com.likelion.beshop.dto;

import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {
    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderTime = order.getOrderTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    //주문아이디
    private Long orderId;

    //주문날짜
    private String orderTime;

    //주문 상태
    private OrderStatus orderStatus;

    //주문 상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<OrderItemDto>();

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}

