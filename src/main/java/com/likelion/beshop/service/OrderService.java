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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.likelion.beshop.constant.OrderStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);
//        System.out.println(authentication.getName());

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
//        System.out.println("지금 가입한 사람의 이메일은? ??????//??" + pageable);

        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);
//        System.out.println("orders" + orders);
//        System.out.println("totalCount" + totalCount);


        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for(Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
//            System.out.println("61" + orderItems.toString());
            for(OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndImageMainYN(orderItem.getItem().getId(), "Y");
//                System.out.println("64" + itemImg.toString());

                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImagePath());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        Member member = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(email, savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.setOrderStatus(CANCEL);
    }

    public Long orders(List<OrderDto> orderDtoList, String email){
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();

        for (OrderDto orderDto : orderDtoList){
            //25나 40이 담겨야 정상인데 냅다 101이 나와버림
            System.out.println("주문 상품에 담긴 아이템 아이디: "+orderDto.getItemId());
            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            orderItemList.add(orderItem);
        }
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }
}
