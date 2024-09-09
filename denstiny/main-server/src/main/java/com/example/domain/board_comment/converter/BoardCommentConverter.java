package com.example.domain.board_comment.converter;

import annotation.Converter;
import com.example.board.BoardEntity;
import com.example.board_comment.BoardCommentEntity;
import com.example.domain.board_comment.controller.model.BoardCommentAddRequest;
import com.example.domain.board_comment.controller.model.BoardCommentResponse;
import com.example.domain.user.controller.model.UserRegisterRequest;
import com.example.domain.user.controller.model.UserRegisterResponse;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Converter
public class BoardCommentConverter {
    public BoardCommentEntity toBoardCommentEntity(BoardCommentAddRequest req){
        return Optional.ofNullable(req)
                .map(it -> {
                    return  BoardCommentEntity.builder()
                            .content(it.getContent())
                            .build();
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT,"BoardCommentAddRequest is null"));
    }

    public BoardCommentResponse toBoardCommentResponse(BoardCommentEntity boardComment) {
        return BoardCommentResponse.builder()
                .boardCommentId(boardComment.getBoardCommentId())
                .content(boardComment.getContent())
                .boardId(boardComment.getBoard().getBoardId())
                .userId(boardComment.getUser().getUserId())
                .build();
    }
}
