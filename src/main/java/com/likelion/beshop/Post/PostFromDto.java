package com.likelion.beshop.Post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostFromDto {

    private Long id;
    private String title;
    private String body;
    private String author;

    private LocalDateTime date;

    private PostStatus status;

    // 엔티티로 변환하는 메서드
//    public Post toEntity() {
//        Post post = new Post();
//        post.setTitle(this.title);
//        post.setBody(this.body);
//        post.setAuthor(this.author);
//        // date와 status는 별도의 로직으로 설정
//        post.setDate(LocalDateTime.now());
//        post.setStatus(PostStatus.WRITE);
//        return post;
//    }
}