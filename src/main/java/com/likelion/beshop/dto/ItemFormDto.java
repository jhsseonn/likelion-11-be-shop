package com.likelion.beshop.dto;

import com.likelion.beshop.constant.ItemSellStatus;
import com.likelion.beshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long code;
    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;
    @NotNull(message = "상품명의 가격을 입력해주세요.")
    private Integer price;
    @NotNull(message = "상품의 재고를 입력해주세요.")
    private Integer num;
    private String content;
    private ItemSellStatus status;
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 수정 시, 상품 이미지 정보를 저장할 리스트 선언

    private List<Long> imageIds = new ArrayList<>(); // 상품 등록 시, 이미지는 저장되지 않은 상태이므로 수정 시에 이미지 아이디를 담을 용도로 상품의 이미지 ID들을 저장할 리스트 선언
    private static ModelMapper modelMapper = new ModelMapper(); // 멤버 변수로 ModelMapper 객체를 추가

    public Item creatItem() { //  modelmapper를 이용해 아이템 등록 위한 메소드 작성
        return modelMapper.map(this, Item.class);
    }

    // modelmapper를 이용해 Item 엔티티 객체를 파라미터로 받아서 Item 객체의 자료형과 멤버변수의 이름이 같을 때 ItemFormDto로 값을 복사해서 반환해주는 메소드 작성
    // static 메소드로 선언하여 객체 생성 여부와 무관하게 호출 가능하도록 설정
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }

}

