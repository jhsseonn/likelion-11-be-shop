package com.likelion.beshop.entity;

import com.likelion.beshop.dto.ProductFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
public class Product {
    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String product_name;
    private Number price;
    private String distributor;
    public static Product createProduct(ProductFormDto productFormDto){
        Product product = new Product();
        product.setProduct_name(productFormDto.getProduct_name());
        product.setPrice(productFormDto.getPrice());
        product.setDistributor(productFormDto.getDistributor());
        return product;
    }
}
