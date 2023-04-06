package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFormDto {
    private String productName;
    private String name;
    private String address;
    private String count;
}
