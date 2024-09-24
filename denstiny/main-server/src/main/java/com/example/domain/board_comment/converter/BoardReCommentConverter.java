package com.example.domain.board_comment.converter;

import annotation.Converter;
import com.example.board_comment.BoardCommentEntity;
import com.example.domain.board_comment.controller.model.BoardReCommentAddRequest;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Converter
public class BoardReCommentConverter {
    public BoardCommentEntity toBoardReCommentEntity(BoardReCommentAddRequest req) {
        return Optional.ofNullable(req)
                .map(it -> {
                    return  BoardCommentEntity.builder()
                            .content(it.getContent())
                            .build();
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT,"BoardReCommentAddRequest is null"));
    }
}
