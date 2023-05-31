package com.likelion.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardReturnDto {
    private String title;
    private String content;
    private NoticeStatus noticeStatus;

    public BoardReturnDto(Board board) {
        this.title = board.getTitle();
        this.content= board.getContent();
        this.noticeStatus= board.getNoticeStatus();
    }
}