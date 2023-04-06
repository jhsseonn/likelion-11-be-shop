package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderRepositoryTest {

    @Autowired
    OrderRepository OrderRepository;

    @Test
    @DisplayName("주문 테스트")
    public void createOrderTest(){
        Order order = new Order();
        order.setName("김한선");
        order.setProductName("텀블러");
        order.setCount("1");
        order.setAddress("서울시 ");
        Order savedOrder = OrderRepository.save(order);
        System.out.println(savedOrder.toString());

    }
}
