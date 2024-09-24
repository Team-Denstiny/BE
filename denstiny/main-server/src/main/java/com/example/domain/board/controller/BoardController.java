package com.example.domain.board.controller;

import api.Api;
import api.Result;
import com.example.domain.board.controller.model.BoardAddRequest;
import com.example.domain.board.controller.model.BoardAddResponse;
import com.example.domain.board.business.BoardBusiness;
import com.example.domain.board.controller.model.BoardGetBoardsResponse;
import com.example.domain.reviewDentist.controller.model.ReviewResponse;
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

    // 게시글 조회
//    @GetMapping("/{userId}/board/category/{category}")
//    public Api<List<BoardGetBoardsResponse>> findBoardsByCategory(
//            @PathVariable("userId") Long userId,
//            @PathVariable("category") Long category,
//            @RequestParam(defaultValue = "0") Integer page,
//            @RequestParam(defaultValue = "10") Integer size
//    ) {
//        List<ReviewResponse> boards = boardBusiness.findBoardsByCategory(userId, category, page, size);
//        return new Api<>(new Result(200, "카테고리에 따른 게시글 조회 성공", "성공"), boards);
//    }

    // 게시글 조회 - 내가 쓴 글
    @GetMapping("/{userId}/board/myboards")
    public ResponseEntity<List<BoardGetBoardsResponse>> getMyBoards(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(boardBusiness.getMyBoards(userId));
    }

    // 게시글 조회 - 내가 좋아요 한 글
    @GetMapping("/{userId}/board/myheartboards")
    public ResponseEntity<List<BoardGetBoardsResponse>> getMyHeartBoards(
            @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(boardBusiness.getMyHeartBoards(userId));
    }

    // 게시글 수정
//    @PatchMapping("/{userId]/board")
//    public ResponseEntity<String> updateBoard(
//            @PathVariable("userId") Long userId,
//            @RequestPart(required = false, name = "images") List<MultipartFile> images,
//            @RequestPart(name = "request") BoardAddRequest boardAddReq
//    ){
//        BoardReponse board = boardBusiness.updateBoard();
//        return ResponseEntity.ok(board.toString());
//    }
}
