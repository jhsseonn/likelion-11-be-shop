package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
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
    private String name;
    private Integer price;
    private Integer count;
    private String description;
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemFormDto itemFormDto) {
        this.name = itemFormDto.getName();
        this.price = itemFormDto.getPrice();
        this.count = itemFormDto.getCount();
        this.description = itemFormDto.getDescription();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

//    public class OutOfStockException extends RuntimeException {
//
//        public OutOfStockException(String msg) {
//            super(msg);
//        }
//    }

    public void removeStock(int quantity) {
        if (this.count < quantity) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.count + ")");
        }
        this.count -= quantity;
    }
}
