package com.example.domain.board_comment.controller;

import api.Api;
import api.Result;
import com.example.domain.board_comment.business.BoardReCommentBusiness;
import com.example.domain.board_comment.controller.model.BoardCommentAddRequest;
import com.example.domain.board_comment.controller.model.BoardReCommentAddRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class BoardReCommentController {
    private final BoardReCommentBusiness boardReCommentBusiness;

    @PostMapping("/{userId}/board/{boardId}/comment/{boardCommentId}")
    public Api<String> addBoardReComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId,
            @PathVariable("boardCommentId") Long boardCommentId,
            @RequestBody BoardReCommentAddRequest boardReCommentAddRequest
    ) {
        String message = boardReCommentBusiness.addBoardReComment(userId, boardId, boardCommentId, boardReCommentAddRequest);
        return new Api<>(new Result(201, "댓글에 대댓글 달기 성공", "성공"), message);
    }

    @DeleteMapping("/{userId}/board/{boardId}/comment/{boardCommentId}/re-comment/{boardReCommentId}")
    public Api<String> deleteBoardComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId,
            @PathVariable("boardCommentId") Long boardCommentId,
            @PathVariable("boardReCommentId") Long boardReCommentId
    ) {
        String message = boardReCommentBusiness.deleteBoardReComment(userId, boardId, boardCommentId, boardReCommentId);
        return new Api<>(new Result(200, "게시글 대댓글 삭제 성공", "성공"), message);
    }

    @PutMapping("/{userId}/board/{boardId}/comment/{boardCommentId}/re-comment/{boardReCommentId}")
    public Api<String> updateBoardReComment(
            @PathVariable("userId") Long userId,
            @PathVariable("boardId") Long boardId,
            @PathVariable("boardCommentId") Long boardCommentId,
            @PathVariable("boardReCommentId") Long boardReCommentId,
            @RequestBody BoardReCommentAddRequest req
    ) {
        String message = boardReCommentBusiness.updateBoardReComment(userId, boardId, boardCommentId, boardReCommentId, req);
        return new Api<>(new Result(200, "게시글 대댓글 수정 성공", "성공"), message);
    }
}
