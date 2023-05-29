package com.likelion.beshop.entity;

import com.likelion.beshop.constant.PostStatus;
import com.likelion.beshop.dto.BoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regTime;

    private String title;

    private String content;
    private PostStatus postStatus;

    public void updatePost(BoardFormDto boardFormDto) {
        this.title = boardFormDto.getTitle();
        this.content = boardFormDto.getContent();
        this.postStatus = boardFormDto.getPostStatus();
    }
}
