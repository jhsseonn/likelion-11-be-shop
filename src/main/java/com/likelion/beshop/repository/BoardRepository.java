package com.likelion.beshop.repository;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {


    List<Board> findAllByBoardStatusOrderByIdDesc(BoardStatus boardStatus);
}
