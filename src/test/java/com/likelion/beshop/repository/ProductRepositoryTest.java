package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductRepositoryTest {
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("상품정보 출력 테스트")
    public void printProductTest(){
        Product product = new Product();
        product.setProduct_name("허니버터칩");
        product.setPrice(1200);
        product.setDistributor("해태제과");
        Product tmpProduct = productRepository.save(product);
        System.out.println(tmpProduct.toString());
    }
}
