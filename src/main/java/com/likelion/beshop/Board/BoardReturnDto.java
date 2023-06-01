package com.likelion.beshop.Board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardReturnDto {

    private Long id;
    private String title;
    private String content;
    private Level level;

    public BoardReturnDto(Board board){
        this.id= board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.level = board.getLevel();
    }
}

