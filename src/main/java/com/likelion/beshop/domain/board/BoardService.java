package com.likelion.beshop.domain.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    //게시물 반환 폼 생성
    public BoardReturnDto createBoardReturnDto(Board board){
        BoardReturnDto boardReturnDto = new BoardReturnDto();
        boardReturnDto.setId(board.getId());
        boardReturnDto.setTitle(board.getTitle());
        boardReturnDto.setContent(board.getContent());
        boardReturnDto.setBoardStatus(BoardStatus.ACTIVE);
        boardReturnDto.setRegTime(board.getRegTime());
        boardReturnDto.setUpdateTime(board.getUpdateTime());
        return boardReturnDto;
    }

    //게시물 엔티티 생성
    public BoardReturnDto createBoard(BoardFormDto boardFormDto){
        Board board = new Board();
        board.setTitle(boardFormDto.getTitle());
        board.setContent(boardFormDto.getContent());
        board.setBoardStatus(BoardStatus.ACTIVE);
        board.setRegTime(LocalDateTime.now());
        board.setUpdateTime(LocalDateTime.now());
        boardRepository.save(board);
        BoardReturnDto boardReturnDto = createBoardReturnDto(board);
        return boardReturnDto;
    }

    //게시물 삭제
    public void deleteBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(board);
    }

    //게시물 수정
    public void updateBoard(Long boardId, String title, String content){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        board.updateTitle(title);
        board.updateContent(content);
    }

    //게시물 리스트 조회
    @Transactional(readOnly = true)
    public List<BoardReturnDto> getBoardList(int boardStatusYN){
        List<BoardReturnDto> boardReturnDtoList = new ArrayList<>();
        List<Board> boardList = new ArrayList<>();

        if (boardStatusYN == 1)
        {
            boardList = boardRepository.findAllByBoardStatusOrderByTitle(BoardStatus.INACTIVE);
        }
        else {
            boardList = boardRepository.findAll();
        }
        for(Board board : boardList){
            BoardReturnDto boardReturnDto = createBoardReturnDto(board);
            boardReturnDtoList.add(boardReturnDto);
        }
        return boardReturnDtoList;
    }

    //상세 게시물 조회
    @Transactional(readOnly = true)
    public BoardReturnDto getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);
        BoardReturnDto boardReturnDto = createBoardReturnDto(board);
        return boardReturnDto;
    }
}
