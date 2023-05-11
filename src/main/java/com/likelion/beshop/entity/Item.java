package com.likelion.beshop.entity;


import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.constant.Role;
import com.likelion.beshop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Entity
//entity 어노테이션 -> JPA에서 관리함
@Table(name="item")
//엔티티와 매핑할 테이블 지정
@Getter
@Setter
@ToString
//lombok 어노테이션 사용
public class Item extends BaseEntity{
    @Id
    //기본키로 설정
    @Column(name="item_id")
    //필드와 매핑할 컬럼이름 설정
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;                        //상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm;                //상품명

    @Column(name = "price", nullable = false)
    private int price;                  //상품 가격

    @Column(nullable = false)
    private int stockNumber;            //재고 수량

    @Column(nullable = false)
    private String itemDetail;                  //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;      //상품 판매 상태

    //LocalDateTime로 하는 게 맞음
    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;

    //상품명 변수를 받아 검색하는 메서드



}
