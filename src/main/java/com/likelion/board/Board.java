package com.likelion.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    //기본키가 있는 포스트가 됨
    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private NoticeStatus noticeStatus;

    public void updateBoard(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content= boardFormDto.getContent();
        this.noticeStatus= boardFormDto.getNoticeStatus();
    }
}
