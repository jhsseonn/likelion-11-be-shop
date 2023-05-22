package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//상품 코드
    @Column(unique = true)
    private String name;
    private int stockNum;
    private String detail;
    @Enumerated(EnumType.STRING)
    private ItemSellStatus sellStatus;

    private int price;
    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNum = itemFormDto.getStockNum();
        this.detail = itemFormDto.getDetail();
        this.sellStatus = itemFormDto.getItemSellStatus();
    }
    public void removeStockNum(int quantity){
        int restStock = this.stockNum - quantity;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량:"+ this.stockNum+ ")");
        }
        this.stockNum = restStock;
    }

    //재고 증가 로직 (주문 취소 시)
    public void addStock(int num){
        this.stockNum += num;
    }

}
