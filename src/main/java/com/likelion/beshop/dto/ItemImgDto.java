package com.likelion.beshop.dto;

import com.likelion.beshop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String imagePath;
    private String imageMainYN;
    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto fromEntity(ItemImg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
