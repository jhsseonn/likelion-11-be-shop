package com.likelion.beshop.entity;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.dto.BoardFormDto;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="board")
@Getter
@Setter
public class Board {
    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull // 제목
    private String title;

    @NotNull // 내용
    private String content;

    // 게시글 타입
    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

    // 게시글 수정
    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
        this.boardStatus = boardFormDto.getBoardStatus();
    }
}
