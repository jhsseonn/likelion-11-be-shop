package com.likelion.beshop.repository;

import com.likelion.beshop.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 작성 테스트")
    public void createPostTest(){
        Post post = new Post();
        post.setTitle("안녕하세요");
        post.setUser_id("한다은");
        post.setContent("오늘 날씨가 좋네요");
        Post savedPost = postRepository.save(post);
        System.out.println(savedPost.toString());
    }

}
