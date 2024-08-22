package com.example.domain.board.business;

import annotation.Business;
import com.example.domain.board.service.BoardService;
import com.example.domain.board.converter.BoardConverter;
import com.example.domain.board.controller.model.BoardRequest;
import com.example.domain.board.controller.model.BoardResponse;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;

import java.util.Optional;


@Business
@AllArgsConstructor
public class BoardBusiness {
    private final BoardService boardService;
    private final BoardConverter boardConverter;

    public BoardResponse addBoard(BoardRequest boardRequest, Long userId){

        return Optional.ofNullable(boardRequest)
                .map(it -> boardConverter.toBoardEntity(it, userId))
                .map(it -> boardService.addBoard(it))
                .map(it -> boardConverter.toBoardResponse(it))
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "addBoard null point"));
    }
}
