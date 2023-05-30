package com.likelion.beshop.dto;

import com.likelion.beshop.constant.BoardStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardFormDto {
    private String title;
    private String content;
    private BoardStatus boardStatus;

}
