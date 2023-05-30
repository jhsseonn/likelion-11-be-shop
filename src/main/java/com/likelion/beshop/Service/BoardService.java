package com.likelion.beshop.Service;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.dto.BoardReturnDto;
import com.likelion.beshop.entity.Board;
import com.likelion.beshop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 등록
    @Transactional
    public BoardReturnDto saveBoard(BoardFormDto boardFormDto) {
        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setBoardStatus(boardFormDto.getBoardStatus());
        boardRepository.save(board);
        return new BoardReturnDto(board);
    }
    // 수정
    @Transactional
    public BoardReturnDto updateBoard(BoardFormDto boardFormDto, Long id) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);
        return new BoardReturnDto(board);
    }

    // 삭제
    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        boardRepository.deleteById(board.getId());
    }

    // 목록 조회
    @Transactional(readOnly=true)
    public List<BoardReturnDto> getBoardList(int boardYN){
        List<Board> boards;

        if(boardYN == 1) {
            boards = boardRepository.findAllByBoardStatusOrderByIdDesc(BoardStatus.ACTIVE);
        }
        else
            boards = boardRepository.findAll();

        return boards.stream().map(BoardReturnDto::new).collect(Collectors.toList());
    }

    // 상세 조회
    @Transactional(readOnly = true)
    public BoardReturnDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new BoardReturnDto(board);
    }
}
