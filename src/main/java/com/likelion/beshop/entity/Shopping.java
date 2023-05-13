package com.likelion.beshop.entity;

import com.likelion.beshop.dto_.MemberFormDto;
import com.likelion.beshop.dto_.ShoppingFormDto;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name ="shopping")
@Getter
@Setter
@ToString

public class Shopping {
    @Id
    @Column(name="shopping_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //기본키가 되어있는거랑 연결된 것

    private String item;
    @Column(unique = true)
    private String price;
    private String size;
    private String color;
    public static Shopping createMember(ShoppingFormDto shoppingFormDto){
        Shopping shopping = new Shopping();
        shopping.setItem(shoppingFormDto.getItem());
        shopping.setPrice(shoppingFormDto.getPrice());
        shopping.setSize(shoppingFormDto.getSize());
        shopping.setColor(shoppingFormDto.getColor());

        return shopping;
    }
}

