package com.likelion.beshop.dto;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.entity.Board;
import org.modelmapper.ModelMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFormDto { // 작성, 수정
    private Long id;
    private String title;
    private String content;
    private BoardStatus boardStatus;

    private static ModelMapper modelMapper = new ModelMapper();

    // BoardFormDto로 입력받은 값(DTO 객체)을 board 엔티티 객체로 변환
    public Board createBoard() {
        return modelMapper.map(this, Board.class);
    }


}
