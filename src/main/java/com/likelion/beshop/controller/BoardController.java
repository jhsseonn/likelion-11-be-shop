package com.likelion.beshop.controller;

import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.dto.BoardReturnDto;
import com.likelion.beshop.entity.Board;
import com.likelion.beshop.repository.BoardRepository;
import com.likelion.beshop.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final BoardRepository boardRepository;

    // 글 생성
    @ResponseBody
    @PostMapping("/api/board/new")
    public ResponseEntity createBoard(@RequestBody BoardFormDto boardFormDto) {
        try {
            Long id = boardService.createBoard(boardFormDto);
            Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            BoardReturnDto boardReturnDto = BoardReturnDto.BoardMapper(board);

            return ResponseEntity.ok(boardReturnDto);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 글 수정
    @PatchMapping("/api/board/edit/{boardId}")
    public ResponseEntity updateBoard(@PathVariable("boardId") Long id, @RequestBody BoardFormDto boardFormDto) {
        try {
            Board board = boardRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            boardService.updateBoard(id, boardFormDto);
            BoardReturnDto boardReturnDto = BoardReturnDto.BoardMapper(board);

            return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 글 삭제
    @DeleteMapping("/api/board/delete/{boardId}")
    public ResponseEntity<HttpStatus> deleteBoard(@PathVariable("boardId") Long id) {
        boardService.deleteBoard(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    // 글 전체 조회
    @GetMapping("/api/boards")
    public ResponseEntity<List<BoardReturnDto>> getBoardList(@RequestParam(name = "QnA", required = false, defaultValue = "0") int QnA) {
        // QUESTION: 0, ANSWER: 1 (default: 0)
        List<BoardReturnDto> boards = boardService.getBoardList(QnA);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    // 글 상세 조회
    @GetMapping("/api/boards/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoard(@PathVariable("boardId") Long id) {
        BoardReturnDto board = boardService.getBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(board);
    }
}
