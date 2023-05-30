package com.likelion.beshop.domain.board;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class BoardFormDto {
    private String title;
    private String content;
    public BoardFormDto(String title, String content){
        this.title = title;
        this.content = content;
    }
}
