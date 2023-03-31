package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ProductFormDto {
    private String product_name;
    private Number price;
    private String distributor;
}
