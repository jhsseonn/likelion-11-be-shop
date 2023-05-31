package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.constant.Role;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //기본키가 있는 아이템이 됨
    @Column(unique = true)
    private String name;

    private Integer price;

    private Integer num;
    private String content;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus status;

//    private LocalDateTime time;
//
//    private LocalDateTime editTime;

    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getName();
        this.price = itemFormDto.getPrice();
        this.num = itemFormDto.getNum();
        this.content= itemFormDto.getContent();
        this.status = itemFormDto.getStatus();
    }


    public void removeStock(int stockNumber){
        int restStock = this.num - stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량 : " + this.num + ")");
        }
        this.num = restStock;
    }

    //반환값은 없다.
    public void addStock(int stockNumber){ // 매개변수로 주문한 수량 받아오기
        this.num += stockNumber; // 상품 재고에 주문 수량만큼 더해주기
    }
}
