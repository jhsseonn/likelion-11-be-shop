package com.likelion.beshop.dto;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer stock;
    private String itemDetail;
    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 수정 시 상품 이미지 정보 저장 리스트
    private List<Long> itemImgIds = new ArrayList<>(); // 등록 시 이미지 아이디 정보 저장 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    // formDto로 입력받은 값(DTO 객체)을 item 엔티티 객체로 변환
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    // item을 itemFormDto 객체로 변환
    public static ItemFormDto ItemMapper(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }
}