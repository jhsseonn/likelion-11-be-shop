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
    //생성자
    public OrderHistDto(Order order){
        //주문아이디
        this.orderId = order.getId();
        //주문날짜
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        //주문 상태
        this.orderStatus = order.getOrderStatus();
    }
    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }
}

