package com.likelion.beshop.dto;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.entity.Board;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardReturnDto {
    private Long id;
    private String title;
    private String content;
    private BoardStatus boardStatus;


    // 생성자

    public BoardReturnDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardStatus = board.getBoardStatus();
    }


}
