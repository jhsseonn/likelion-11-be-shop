package com.likelion.beshop.dto;

import com.likelion.beshop.constant.OrderStatus;
import com.likelion.beshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 주문 정보를 담을 Dto
@Getter
@Setter
public class OrderHistDto {
    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    // 주문한 상품 정보 리스트 (상품을 여러 종류 주문할 수도 있으므로)
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    // OrderItemDto를 매개변수로 받아와 주문한 상품 리스트에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }

}
