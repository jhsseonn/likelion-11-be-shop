package com.likelion.beshop.Post;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class PostListDto {

    private Long id;
    private String title;

    private static ModelMapper modelMapper = new ModelMapper();
    public static PostListDto of(PostList postList){return modelMapper.map(postList, PostListDto.class);}
}
