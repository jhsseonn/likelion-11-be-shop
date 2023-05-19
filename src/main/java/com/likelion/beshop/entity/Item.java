package com.likelion.beshop.entity;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.dto.ItemFormDto;
import com.likelion.beshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="item") // 엔티티와 매핑할 테이블 지정
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    // 상품 코드
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // 상품명
    @Column(nullable = false)
    private String itemNm;

    // 가격
    @Column(nullable = false)
    private int price;

    // 재고 수량
    @Column(nullable = false)
    private int stockNumber;

    // 상품 상세설명
    @Column(nullable = false)
    private String itemDetail;

    // 상품 판매 상태 => enum
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    // BaseEntity가 등록일, 수정일, 등록자, 수정자를 모두 갖고 있어서 등록시간, 수정시간 멤버변수는 삭제
//    // 등록 시간
//    private LocalDateTime registerTime;
//    // 수정 시간
//    private LocalDateTime updateTime;

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

}
