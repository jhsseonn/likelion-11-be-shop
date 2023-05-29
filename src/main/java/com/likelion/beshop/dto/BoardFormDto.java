package com.likelion.beshop.dto;

import com.likelion.beshop.constant.PostStatus;
import com.likelion.beshop.entity.BoardEntity;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardFormDto {
    private String title;
    private String content;
    private PostStatus postStatus;
    private static ModelMapper modelMapper = new ModelMapper();
    public BoardEntity createPost() {//아이템 등록 위한 메소드
        return modelMapper.map(this, BoardEntity.class);
    }
}
