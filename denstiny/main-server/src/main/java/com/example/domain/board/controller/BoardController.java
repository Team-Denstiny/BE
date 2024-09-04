package com.example.domain.board.controller;

import com.example.domain.board.controller.model.BoardAddRequest;
import com.example.domain.board.controller.model.BoardAddResponse;
import com.example.domain.board.business.BoardBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class BoardController {
    private final BoardBusiness boardBusiness;

    // 게시글 생성
    @PostMapping("/{userId}/board")
    public ResponseEntity<String> addBoard(
            @PathVariable("userId") Long userId,
            @RequestPart(required = false, name = "images") List<MultipartFile> images,
            @RequestPart(name = "request") BoardAddRequest boardAddReq
    ){
        BoardAddResponse boardRes = boardBusiness.addBoard(boardAddReq, images, userId);
        return ResponseEntity.ok(boardRes.toString());
    }

    // 게시글 삭제
    @DeleteMapping("/{userId}/board/{boardId}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId) {
        return ResponseEntity.ok(boardBusiness.deleteBoard(userId, boardId));
    }

//    // 게시글 수정
//    @PutMapping("/{id}")
//    public ResponseEntity<BoardEntity> updateBoard(@PathVariable Long id, @RequestBody BoardEntity updatedBoard) {
//        Optional<BoardEntity> board = boardService.updateBoard(id, updatedBoard);
//        return board.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
}
