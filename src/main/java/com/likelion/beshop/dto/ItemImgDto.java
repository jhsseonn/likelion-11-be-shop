package com.likelion.beshop.dto;

import com.likelion.beshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    private Long id;
    private String imageName;
    private String originalImageName;
    private String imagePath;
    private String repImage;

    private static ModelMapper modelMapper = new ModelMapper(); // 멤버 변수로 ModelMapper 객체를 추가

    // Itemlmg 엔티티 객체를 파라미터로 받아서 Itemlmg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemlmgDto로 값을 복사해서 반환해주는 메소드 작성
    // static 메소드로 선언하여 객체 생성 여부와 무관하게 호출 가능하도록 설정
    public static ItemImgDto convertToDto(ItemImg itemImg) {
        ItemImgDto itemImgDto = modelMapper.map(itemImg, ItemImgDto.class);
        return itemImgDto;
    }

}

