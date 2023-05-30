package com.likelion.beshop.domain.board;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardReturnDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private BoardStatus boardStatus;
}