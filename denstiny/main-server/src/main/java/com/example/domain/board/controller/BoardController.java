package com.example.domain.board.controller;


import com.example.domain.board.controller.model.BoardRequest;
import com.example.domain.board.controller.model.BoardResponse;
import com.example.domain.board.service.BoardService;
import com.example.domain.board.business.BoardBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class BoardController {

    private final BoardService boardService;
    private final BoardBusiness boardBusiness;

    // 게시글 생성
    @PostMapping("/{userId}/board")
    public ResponseEntity<String> addBoard(
            @PathVariable("userId") Long userId,
            @RequestBody BoardRequest boardReq
    ){
        BoardResponse boardRes = boardBusiness.addBoard(boardReq, userId);
        return ResponseEntity.ok(boardRes.toString());
    }

//    // 게시글 수정
//    @PutMapping("/{id}")
//    public ResponseEntity<BoardEntity> updateBoard(@PathVariable Long id, @RequestBody BoardEntity updatedBoard) {
//        Optional<BoardEntity> board = boardService.updateBoard(id, updatedBoard);
//        return board.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // 게시글 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
//        boardService.deleteBoard(id);
//        return ResponseEntity.noContent().build();
//    }
}
