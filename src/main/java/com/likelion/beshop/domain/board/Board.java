package com.likelion.beshop.domain.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@Entity
@Table(name = "board")
@EntityListeners(value={AuditingEntityListener.class})
public class Board {
    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;
    @LastModifiedDate
    private LocalDateTime updateTime;
    @Enumerated(EnumType.STRING)
    private BoardStatus boardStatus;

    public void updateTitle(String title) {
        this.title = title;
    }
    public void updateContent(String content){
        this.content = content;
    }
}