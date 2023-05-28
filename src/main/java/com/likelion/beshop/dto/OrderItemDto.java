package com.likelion.beshop.dto;

import com.likelion.beshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

//데이터를 불러오고 세팅하는 데에 사용하는 어노테이션
@Getter
@Setter
public class OrderItemDto {

    // OrderItemDto 클래스의 생성자로 OrderItem 객체와 이미지 경로를 파라미터로 받아서 멤버 변수값을 세팅
    public OrderItemDto(OrderItem orderItem, String imagePath){
        this.name = orderItem.getItem().getName();
        this.num = orderItem.getNum();
        this.price = orderItem.getPrice();
        this.imagePath = imagePath;
    }

    //OrderItem과 동일한 이름으로 선언
    private String name; //상품명 -> Item 에서 가져옴
    private int num; //주문 수량

    private int price; //주문 금액
    private String imagePath; //상품 이미지 경로 -> ItemImg 에서 가져옴

}
