package com.likelion.beshop.dto;

import com.likelion.beshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    // id
    private Long id;

    // 이미지 파일명
    private String imgName;

    // 원본 이미지 파일명
    private String oriImgName;

    // 이미지 조회 경로
    private String imgUrl;

    // 대표 이미지 여부
    private String repImgYn;

    // 멤버 변수로 ModelMapper 객체 추가
    private static ModelMapper modelMapper = new ModelMapper();

    // ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수 이름이 같을 때 ItemImgDto로 값 복사해서 반환하는 메서드
    // static 메소드 => 객체(ItemImgDto) 생성 여부와 무관하게 호출 가능
    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);
    }

}
