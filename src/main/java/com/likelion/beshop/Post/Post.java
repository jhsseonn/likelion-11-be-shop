package com.likelion.beshop.Post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name ="post")
public class Post{

    @Id
    @Column(name ="post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String body;
    private String author;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    public void updatePost(PostFromDto postFromDto) {

        this.title = postFromDto.getTitle();
        this.body = postFromDto.getBody();
        this.author = postFromDto.getAuthor();
        this.date = postFromDto.getDate();
        this.status = postFromDto.getStatus();

    }


}