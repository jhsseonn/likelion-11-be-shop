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
    private Long id;//상품코드
    private String itemName;//상품명
    private Integer price;//가격
    private String detail;//상세설명
    private Integer stockNum;//재고수
    private ItemSellStatus itemSellStatus;//판매상태
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();//수정 시 이미지 정보 저장 리스트
    private List<Long> itemImgIds = new ArrayList<>();//상품 등록 시 이미지 id저장 리스트
    private static ModelMapper modelMapper = new ModelMapper();
    public Item createItem() {//아이템 등록 위한 메소드
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){// item객체를 받아 비교해 dto로 값 복사해 반환해주는 메소드
        return modelMapper.map(item,ItemFormDto.class);
    }
}
