package com.likelion.beshop.entity;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.dto.BoardReturnDto;
import lombok.*;

import javax.persistence.*;
@Entity
@Table(name="board")
@Getter
@Setter
@ToString
public class Board {
    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;



    public void updateBoard(BoardFormDto boardFormDto) {
        this.title=boardFormDto.getTitle();
        this.content=boardFormDto.getContent();
        this.boardStatus=boardFormDto.getBoardStatus();
    }

}
