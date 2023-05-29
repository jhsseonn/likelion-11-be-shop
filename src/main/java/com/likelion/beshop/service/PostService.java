package com.likelion.beshop.service;

import com.likelion.beshop.constant.PostStatus;
import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.dto.BoardReturnDto;
import com.likelion.beshop.entity.BoardEntity;
import com.likelion.beshop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor

public class PostService {
    private final BoardRepository boardRepository;
    @Transactional
    public Long createPost(BoardFormDto boardFormDto){//게시물 생성
        BoardEntity post = boardFormDto.createPost();
        post.setTitle(boardFormDto.getTitle());
        post.setContent(boardFormDto.getContent());
        post.setPostStatus(boardFormDto.getPostStatus());
        boardRepository.save(post);
        return post.getId();

    }
    @Transactional(readOnly = true)
    public List<BoardEntity> getAllPosts(int checkActive){//모든 게시물 가져오기
        List<BoardEntity> boardList;

        if(checkActive == 1){
            boardList = boardRepository.findAllByPostStatusOrderByRegTimeDesc(PostStatus.ACTIVE);
        }
        else{
            boardList = boardRepository.findAll();
        }
        return boardList;

    }
    @Transactional(readOnly = true)
    public BoardEntity getPostById(Long id){//특정 게시물 가져오기
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return boardEntity;

    }
    @Transactional
    public Long updatePost(Long id , BoardFormDto boardFormDto){//게시물 수정
        BoardEntity post = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        post.updatePost(boardFormDto);
        boardRepository.save(post);
        return id;
    }
    @Transactional
    public void deletePost(Long id){//게시물 삭제
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(boardEntity);

    }


}
