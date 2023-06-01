package com.likelion.beshop.Board;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public BoardReturnDto saveBoard(BoardFormDto boardFormDto){

        Board board = Board.builder()
                .title(boardFormDto.getTitle())
                .content(boardFormDto.getContent())
                .level(Level.SILVER)
                .build();
        boardRepository.save(board);
        return new BoardReturnDto(board);
        //프론트엔드한데 리턴해줗 값
    }

    //board 수정
    @Transactional
    public Long updateBoard(Long id, BoardFormDto boardFormDto){
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        board.updateBoard(boardFormDto);
        boardRepository.save(board);
        return id;
    }



    //board 삭제
    @Transactional
    public void deleteBoard(Long id){
        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        boardRepository.delete(board);
    }


    //board 목록/전체 조회
    //받아오고 , 선언하고
    @Transactional(readOnly = true)
    public List<Board> getBoardList(int noticeYN){
        List<Board> board;

        //noticeYN이 1이면 GOLD레벨인 것만 필터링
        if(noticeYN == 1){
            board = boardRepository.findAllByLevel(Level.GOLD);
        }
        else {
            board = boardRepository.findAll();
        }
        return board;
    }



    //board 상세 조회
    @Transactional(readOnly = true)
    public BoardReturnDto getBoard(Long id){

        Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new BoardReturnDto(board);
    }


}

