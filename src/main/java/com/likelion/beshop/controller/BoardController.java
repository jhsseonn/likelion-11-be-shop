package com.likelion.beshop.controller;

import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.entity.BoardEntity;
import com.likelion.beshop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final PostService postService;
    @PostMapping("/post")// 글 작성 , 저장
    public ResponseEntity<Long> createPost(@RequestBody BoardFormDto post) {
        Long kId = postService.createPost(post);
        return new ResponseEntity<>(kId, HttpStatus.CREATED);
    }

    @GetMapping("/post")// 모든 글 불러오기
    public ResponseEntity<List<BoardEntity>> getAllPosts(@RequestParam(name = "checkActive",required = false,defaultValue = "0")int checkActive) {
        List<BoardEntity> posts = postService.getAllPosts(checkActive);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @GetMapping("/post/{id}")//특정 글 불러오기
    public ResponseEntity<BoardEntity> getPostById(@PathVariable Long id) {
        BoardEntity post = postService.getPostById(id);
        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/post/{id}")// 게시글 수정
    public ResponseEntity<Long> updatePost(@PathVariable Long id, @RequestBody BoardFormDto post) {
        Long kId = postService.updatePost(id,post);
        return new ResponseEntity<>(kId, HttpStatus.OK);
    }
    @DeleteMapping("/post/{id}")//게시글 삭제
    public ResponseEntity<Long> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}

