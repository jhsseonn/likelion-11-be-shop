package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Member;
import com.likelion.beshop.entity.Shopping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ShoppingRepositoryTest {
    @Autowired
    ShoppingRepository shoppingRepository;

    @Test
    @DisplayName("회원가입테스트")
    public void createMemberTest() {
        Shopping shopping = new Shopping();
        shopping.setItem("테이퍼드 히든 밴딩 슬렉스");
        shopping.setPrice("39,900원");
        shopping.setSize("30 size");
        shopping.setColor(("차콜 그레이"));
        Shopping savedShopping = shoppingRepository.save(shopping);
        System.out.println((savedShopping.toString()));
    }
}
