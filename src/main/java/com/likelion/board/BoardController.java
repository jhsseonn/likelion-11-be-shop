package com.likelion.board;


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


    // 글 작성 (저장)
    @PostMapping("/api/board")
    public ResponseEntity<BoardReturnDto> createBoard(@RequestBody BoardFormDto boardFormDto) {
        BoardReturnDto board = boardService.saveBoard(boardFormDto);
        return ResponseEntity.ok(board);
    }

    // 글 수정 위한 상세 조회
    @GetMapping("/api/board/edit/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoardForUpdate(@PathVariable("boardId")Long boardId) {
        // 여기서는 동일한 로직을 불러오는 것이 맞지만 사용자가 있을 경우에는 getBoard가 아닌 사용자를 검증하는 로직을 불러와야함.
        BoardReturnDto boardReturnDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    // 글 수정
    @PutMapping("/api/board/edit/{boardId}")
    public ResponseEntity<BoardReturnDto> updateBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardFormDto boardFormDto) {
        BoardReturnDto boardReturnDto = boardService.updateBoard(boardId, boardFormDto);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    // 글 삭제
    @DeleteMapping("/api/board/{boardId}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 글 목록 조회
    @GetMapping("/api/board")
    public ResponseEntity<List<BoardReturnDto>> getBoardList(@RequestParam(name = "noticeYN", required= false, defaultValue = "0") int noticeYN) {
        // 공지여부 1 = 공지글만 보이도록 필터링 (공지여부 0, 이외 = 모든 글)
        List<BoardReturnDto> boards = boardService.getBoardList(noticeYN);
        return ResponseEntity.status(HttpStatus.OK).body(boards);
    }

    // 글 상세 조회
    @GetMapping("/api/board/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoard(@PathVariable("boardId") Long boardId){
        BoardReturnDto boardReturnDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }


}
