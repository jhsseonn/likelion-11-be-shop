package com.likelion.beshop.entity;

import com.likelion.beshop.dto.PostFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="post")
@Getter @Setter
@ToString

public class Post {
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //기본키가 있는 포스트가 됨
    private String title;
    @Column(unique = true)
    private String user_id;
    private String content;

    public static Post creatMember(PostFormDto postFormDto) {
        Post post = new Post();
        post.setTitle(postFormDto.getTitle());
        post.setUser_id(postFormDto.getUser_id());
        post.setContent(postFormDto.getContent());

        return post;
    }
}
