package com.likelion.beshop.domain.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    //전체 조회
    @GetMapping(value = "")
    public ResponseEntity<List<BoardReturnDto>> getBoards(@RequestParam(name = "boardYN", required = false, defaultValue = "0")int boardYN){
        List<BoardReturnDto> boardReturnDtoList = boardService.getBoardList(boardYN);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDtoList);
    }

    //상세 조회
    @GetMapping(value = "/{boardId}")
    public ResponseEntity<BoardReturnDto> getBoard(@PathVariable("boardId")Long boardId,  Model model){
        BoardReturnDto boardReturnDto = boardService.getBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(boardReturnDto);
    }

    //수정
    @PutMapping(value = "/{boardId}")
    public @ResponseBody ResponseEntity updateBoard(@PathVariable("boardId")Long boardId, @RequestBody BoardFormDto boardFormDto){
        boardService.updateBoard(boardId, boardFormDto.getTitle(), boardFormDto.getContent());
        return new ResponseEntity<Long>(boardId, HttpStatus.OK);
    }

    //삭제
    @DeleteMapping(value = "/{boardId}")
    public @ResponseBody ResponseEntity deleteBoard(@PathVariable("boardId")Long boardId){
        boardService.deleteBoard(boardId);
        return new ResponseEntity<String>("삭제가 완료됐습니다.", HttpStatus.OK);
    }

    //생성
    @PostMapping(value = "")
    public ResponseEntity<BoardReturnDto> createBoard(@RequestBody BoardFormDto boardFormDto){
        BoardReturnDto boardReturnDto = boardService.createBoard(boardFormDto);
        return ResponseEntity.ok(boardReturnDto);
    }
}