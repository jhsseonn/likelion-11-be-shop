package com.likelion.beshop.entity;

import com.likelion.beshop.dto.ProductFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="product")
@Getter@Setter@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private Long id;
    private String name;
    private String price;
    private String vendor;

    public static Product createProduct(ProductFormDto productFormDto){
        Product product = new Product();
        product.setName(productFormDto.getName());
        product.setPrice(productFormDto.getPrice());
        product.setVendor(productFormDto.getVendor());
        return product;
    }
}
