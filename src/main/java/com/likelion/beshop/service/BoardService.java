package com.likelion.beshop.service;

import com.likelion.beshop.constant.BoardStatus;
import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.dto.BoardReturnDto;
import com.likelion.beshop.entity.Board;
import com.likelion.beshop.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 생성
    public Long createBoard(BoardFormDto boardFormDto) throws Exception {

        Board board = boardFormDto.createBoard();
        boardRepository.save(board);

        return board.getId();
    }

    // 게시글 수정
    public Long updateBoard(Long id, BoardFormDto boardFormDto) throws Exception {
        // 게시글 찾기
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        board.updateBoard(boardFormDto);
        return board.getId();

    }

    // 게시글 삭제
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(board);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<BoardReturnDto> getBoardList(int QnA) {

        List<Board> boardList;
        // 전체 게시글 엔티티 리스트
        if (QnA == 1) { // QUESTION 이면 = 1
            boardList = boardRepository.findAllByBoardStatusOrderByIdDesc(BoardStatus.QUESTION);
        }
        else if (QnA == 2){ // ANSWER 이면 = 2
            boardList = boardRepository.findAllByBoardStatusOrderByIdDesc(BoardStatus.ANSWER);
        }
        else { // 전체 조회
            boardList = boardRepository.findAll();
        }

        // 게시글 dto 리스트 생성
        List<BoardReturnDto> boardDtoList = new ArrayList<>();

        // 전체 게시글 엔티티를 dto로 변환하여 dto 리스트에 추가
        for (Board board : boardList) {
            BoardReturnDto boardReturnDto = BoardReturnDto.BoardMapper(board);
            boardDtoList.add(boardReturnDto);
        }

        // BoardReturnDto 리스트 반환
        return boardDtoList;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public BoardReturnDto getBoard(Long id) {
        // id로 게시글 찾기
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        // 게시글 엔티티 dto로 변환
        BoardReturnDto boardReturnDto = BoardReturnDto.BoardMapper(board);

        // BoardReturnDto 리스트 반환
        return boardReturnDto;
    }
}
