package com.example.domain.board.converter;

import annotation.Converter;
import java.util.Optional;

import com.example.board.BoardEntity;
import com.example.domain.board.controller.model.BoardRequest;
import com.example.domain.board.controller.model.BoardResponse;
import error.ErrorCode;
import exception.ApiException;


@Converter
public class BoardConverter {
    public BoardEntity toBoardEntity(BoardRequest request, Long userId){
        return Optional.ofNullable(request)
                .map(it -> {
                    return BoardEntity.builder()
                            .title(request.getTitle())
                            .content(request.getContent())
                            .category(request.getCategory())
                            .writer(userId)
                            .build();
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "BoardRequest is null"));
    }

    public BoardResponse toBoardResponse(BoardEntity board){
        return Optional.ofNullable(board)
                .map(it -> {
                    return BoardResponse.builder()
                            .boardId(it.getBoardId())
                            .writer(it.getWriter())
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "BoardEntity is null"));
    }
}
