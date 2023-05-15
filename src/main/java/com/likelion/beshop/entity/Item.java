package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends Base {
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String itemName;
    @NotNull
    private Integer price;
    @NotNull
    private Integer stock;
    @NotNull
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stock = itemFormDto.getStock();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    private void removeStock(int orderStock) { // 주문 수량을 매개변수로 받아옴

        // 현재 재고보다 주문 수량이 많다면 오류 메시지 출력
        if (this.stock < orderStock) {
            throw new OutOfStockException("상품의 재고가 부족합니다."
                    + "(현재 재고 수량: " + this.stock + ")");
        }
        // 정상 주문이면 재고에서 주문 수량 감소시키고 업데이트
        this.stock = this.stock - orderStock;
    }

}
