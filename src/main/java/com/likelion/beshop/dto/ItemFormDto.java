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

    // id(상품코드)
    private Long id;

    // 상품명
    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    // 가격
    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    // 상품 상세설명
    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    // 재고수
    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    // 판매상태
    private ItemSellStatus itemSellStatus;

    // 수정 시, 상품 이미지 정보를 저장하는 리스트 선언
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    // 상품 등록 시, 이미지는 저장되지 않은 상태이므로 수정 시에 이미지를 담을 용도로 상품의 이미지 ID들을 저장할 리스트 선언
    private List<Long> itemImgIds = new ArrayList<>();

    // 멤버 변수로 ModelMapper 객체 추가
    private static ModelMapper modelMapper = new ModelMapper();

    // modelMapper를 이용한 아이템 등록 메소드
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    // modelMapper를 이용한 Item 엔티티 객체를 파라미터로 받아서 Item 객체의 자료형과 멤버변수 이름이 같을 때 ItemFormDto로 값을 복사해서 반환해주는 메소드
    // static 메소드 => 객체 생성 여부와 무관하게 호출 가능
    public static ItemFormDto of(Item item){
        return modelMapper.map(item,ItemFormDto.class);
    }

}
