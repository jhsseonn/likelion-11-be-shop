package com.likelion.beshop.repository;

import com.likelion.beshop.constant.PostStatus;
import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity,Long> {
    List<BoardEntity> findAllByPostStatusOrderByRegTimeDesc(PostStatus postStatus);
}
