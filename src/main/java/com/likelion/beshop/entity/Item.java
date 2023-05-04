package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item") // 엔티티와 매핑할 테이블 지정
@Getter
@Setter
@ToString
public class Item {

    // 상품 코드
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // 상품명
    @Column(unique = true, nullable = false) // unique 속성 부여(type : boolean)
    private String name;

    // 가격
    @Column(nullable = false)
    private int price;

    // 재고 수량
    @Column(nullable = false)
    private int num;

    // 상품 상세설명
    @Column(nullable = false)
    private String detail;

    // 상품 판매 상태
    @Enumerated(EnumType.STRING)
    private ItemSellStatus status;

    // 등록 시간
    private LocalDateTime registerTime;
    // 수정 시간
    private LocalDateTime updateTime;
}
