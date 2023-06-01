package com.likelion.beshop.Post;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "postList")
public class PostList {

    @Id
    @Column(name = "postList")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
}
