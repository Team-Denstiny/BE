package com.example.domain.board.converter;

import annotation.Converter;
import java.util.Optional;

import com.example.board.BoardEntity;
import com.example.domain.board.controller.model.BoardAddRequest;
import com.example.domain.board.controller.model.BoardAddResponse;
import com.example.domain.board.controller.model.BoardGetBoardsResponse;
import error.ErrorCode;
import exception.ApiException;


@Converter
public class BoardConverter {
    public BoardEntity toBoardEntity(BoardAddRequest request, Long userId){
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

    public BoardAddResponse toBoardResponse(BoardEntity board){
        return Optional.ofNullable(board)
                .map(it -> {
                    return BoardAddResponse.builder()
                            .boardId(it.getBoardId())
                            .writer(it.getWriter())
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "BoardEntity is null"));
    }

    public BoardGetBoardsResponse toBoardGetMyBoardsResponse(BoardEntity board){
        return Optional.ofNullable(board)
                .map(it -> {
                    return  BoardGetBoardsResponse.builder()
                            .boardId(it.getBoardId())
                            .title(it.getTitle())
                            .content(it.getContent())
                            .viewCount(it.getViewCount())
                            .writer(it.getWriter())
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "BoardEntity is null"));
    }
}