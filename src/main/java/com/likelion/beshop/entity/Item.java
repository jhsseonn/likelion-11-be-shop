package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemFormDto;
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

}
