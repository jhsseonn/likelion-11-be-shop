package com.likelion.beshop.Post;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostReturnDto {

    private Long id;
    private String title;
    private String body;
    private String author;
    private LocalDateTime date;
    private PostStatus status;

//    public PostReturnDto(Post post) {
//        this.id = post.getId();
//        this.title = post.getTitle();
//        this.body = post.getBody();
//        this.author = post.getAuthor();
//        this.date = post.getDate();
//        this.status = post.getStatus();
//    }

    private static ModelMapper modelMapper = new ModelMapper();

    public Post createPost() { return modelMapper.map(this, Post.class);}

    public static PostFromDto of(Post post) { return modelMapper.map(post, PostFromDto.class);}

}