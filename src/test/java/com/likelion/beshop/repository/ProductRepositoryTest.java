package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품등록 테스트")
    public void createProductTest(){
        Product product = new Product();
        Product tmp;
        product.setName("허니버터칩");
        product.setPrice("1600원");
        product.setVendor("해태제과");
        tmp = productRepository.save(product);
        System.out.println(tmp.toString());
    }

}
