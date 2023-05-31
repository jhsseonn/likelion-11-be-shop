package com.likelion.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // 글 작성 (저장)
    @Transactional
    public BoardReturnDto saveBoard(BoardFormDto boardFormDto) {

        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setNoticeStatus(boardFormDto.getNoticeStatus());
        boardRepository.save(board);

        return new BoardReturnDto(board);
    }

    // 글 수정
    @Transactional
    public BoardReturnDto updateBoard(Long id, BoardFormDto boardFormDto) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);
        // 수정할 때 원래 인증 절차 필요하지만, 사용자 없으므로 조건 생략
        return new BoardReturnDto(board);
    }

    // 글 삭제
    @Transactional
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        // 수정할 때 원래 인증 절차 필요하지만, 사용자 없으므로 조건 생략
        boardRepository.deleteById(board.getId());
    }
    
    // 글 목록 조회
    @Transactional(readOnly = true) // 읽기 전용 설정
    public List<BoardReturnDto> getBoardList(int noticeYN){
        List<Board> boards;

        if(noticeYN == 1) {
            boards = boardRepository.findAllByNoticeStatusOrderByIdDesc(NoticeStatus.NOTICE);
        }

        else{
            boards = boardRepository.findAll();
        }

        return boards.stream().map(BoardReturnDto::new).collect(Collectors.toList());
    }

    // 글 상세 조회
    @Transactional(readOnly = true)
    public BoardReturnDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        // 수정할 때 원래 인증 절차 필요하지만, 사용자 없으므로 조건 생략
        return new BoardReturnDto(board);
    }

}
