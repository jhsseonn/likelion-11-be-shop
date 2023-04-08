package com.likelion.beshop.entity;

import com.likelion.beshop.dto.OrderFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="orderinfo")
@Getter
@Setter
@ToString
public class Order {

    @Id
    @Column(name="orderinfo_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String productName;
    private String count;
    private String address;

    public static Order createOrder(OrderFormDto orderFormDto){
        Order order = new Order();
        order.setName(orderFormDto.getName());
        order.setAddress(orderFormDto.getAddress());
        order.setCount(orderFormDto.getCount());
        order.setProductName(order.getProductName());


        return order;
    }

}
