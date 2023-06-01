package com.likelion.beshop.Board;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

//@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final BoardRepository boardRepository;

    //글 작성 (저장)
    @PostMapping("/api/board")
    public ResponseEntity<BoardReturnDto> createPost(@RequestBody BoardFormDto boardFormDto){
        BoardReturnDto board = boardService.saveBoard(boardFormDto);
        return ResponseEntity.ok(board);
    }

    //글 수정위한 상세 조회
    @GetMapping("/api/board/edit/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoardForUpdate(@PathVariable("boardId") Long boardId){
        BoardReturnDto boardReturnDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    //글 수정
    @PutMapping(value = "/api/board/edit/{id}")
    public ResponseEntity<BoardReturnDto> updateBoard(@PathVariable Long id, @RequestBody BoardFormDto boardFormDto){
        Long Id = boardService.updateBoard(id, boardFormDto);
        BoardReturnDto boardReturnDto = boardService.getBoard(Id);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    //글 삭제
    @DeleteMapping("/api/board/{id}")
    public ResponseEntity<HttpStatus> deleteBoard(@PathVariable("boardId") Long boardId){
        boardService.deleteBoard(boardId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //글 목록 조회
    @GetMapping("/api/board")
    public ResponseEntity<List<Board>> getBoardList(@RequestParam(name="noticeYN", required = false, defaultValue = "0")int noticeYN){
        //1이면 GOLD 레벨이 쓴 글만 보이도록 필터링(0, 이외 = SILVER 레벨이 쓴 글만 보임)
        List<Board> board = boardService.getBoardList(noticeYN);
        return new ResponseEntity<>(board,HttpStatus.OK);
    }


    //글 상세 조회
    @GetMapping("/api/board/{id}")
    public ResponseEntity<BoardReturnDto> getBoardById(@PathVariable Long id){
        BoardReturnDto boardReturnDto = boardService.getBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }



}

