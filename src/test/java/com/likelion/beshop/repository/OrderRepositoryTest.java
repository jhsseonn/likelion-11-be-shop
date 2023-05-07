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


}
