package com.likelion.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardFormDto {
    private String title;
    private String content;
    private NoticeStatus noticeStatus;
}