package com.example.domain.board_comment.controller;


import api.Api;
import api.Result;
import com.example.domain.board_comment.business.BoardCommentBusiness;
import com.example.domain.board_comment.controller.model.BoardCommentAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class BoardCommentController {
    private final BoardCommentBusiness boardCommentBusiness;
    @PostMapping("/{userId}/board/{boardId}/comment")
    public Api<String> addBoardComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId,
            @RequestBody BoardCommentAddRequest req
    ){
        String message = boardCommentBusiness.addBoardComment(userId, boardId, req);
        return new Api<>(new Result(201, "게시판 댓글 추가 성공", "성공"), message);
    }

    @DeleteMapping("/{userId}/board/{boardId}/comment/{boardCommentId}")
    public Api<String> deleteBoardComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId,
            @PathVariable("boardCommentId") Long boardCommentId
    ) {
        String message = boardCommentBusiness.deleteBoardComment(userId, boardId, boardCommentId);
        return new Api<>(new Result(200, "게시글 댓글 삭제 성공", "성공"), message);
    }

    @PutMapping("/{userId}/board/{boardId}/comment/{boardCommentId}")
    public Api<String> updateBoardComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId,
            @PathVariable("boardCommentId") Long boardCommentId,
            @RequestBody BoardCommentAddRequest req
    ) {
        String message = boardCommentBusiness.updateBoardComment(userId, boardId, boardCommentId, req);
        return new Api<>(new Result(200, "게시글 댓글 수정 성공", "성공"), message);
    }
}
