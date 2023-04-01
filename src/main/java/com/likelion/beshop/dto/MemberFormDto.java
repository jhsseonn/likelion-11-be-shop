package com.likelion.beshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormDto {
    // 변수 선언
    private String name;
    private String email;
    private String password;
    private String address;
}
