package com.likelion.beshop.Board;




import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name="board")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Level level;

    public Board() {
        super();
    }

    public void updateBoard(BoardFormDto boardFormDto){
        this.content=boardFormDto.getContent();
        this.title = boardFormDto.getTitle();
    }
}
