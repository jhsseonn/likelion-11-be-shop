package com.likelion.beshop.controller;

import com.likelion.beshop.Service.BoardService;
import com.likelion.beshop.dto.BoardFormDto;
import com.likelion.beshop.dto.BoardReturnDto;
import com.likelion.beshop.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 글 작성
    @PostMapping("/api/board")
    public ResponseEntity<BoardReturnDto> createBoard(@RequestBody BoardFormDto boardFormDto) {
        BoardReturnDto board = boardService.saveBoard(boardFormDto);
        return ResponseEntity.ok(board);
    }

    // 상세조회
    @GetMapping("/api/board/edit/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoardForUpdate(@PathVariable("boardId")Long boardId) {
        BoardReturnDto boardReturnDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    // 수정
    @PutMapping("/api/board/edit/{boardId}")
    public ResponseEntity<BoardReturnDto> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardFormDto boardFormDto) {
        BoardReturnDto boardReturnDto = boardService.updateBoard(boardFormDto, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    // 삭제
    @DeleteMapping("/api/board/{boardId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 목록 조회
    @GetMapping("/api/board")
    public ResponseEntity<List<BoardReturnDto>> getBoardList(@RequestParam(name = "boardYN", required= false, defaultValue = "0") int boardYN) {
        List<BoardReturnDto> boards = boardService.getBoardList(boardYN);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    // 상세 조회
    @GetMapping("/api/board/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoard(@PathVariable("boardId") Long boardId){
        BoardReturnDto boardReturnDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }


}
