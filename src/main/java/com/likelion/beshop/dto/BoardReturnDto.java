package com.likelion.beshop.dto;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.entity.Board;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardReturnDto { // 상세 조회, 상세 조회
    private Long id;
    private String title;
    private String content;
    private BoardStatus boardStatus;

    private static ModelMapper modelMapper = new ModelMapper();

    public static BoardReturnDto BoardMapper(Board board) {
        return modelMapper.map(board, BoardReturnDto.class);
    }

}
