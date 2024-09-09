package com.example.domain.board.converter;

import annotation.Converter;
import com.example.board.BoardEntity;
import com.example.board_dental_type.BoardDentalTypeEntity;
import com.example.domain.board.controller.model.BoardAddRequest;
import error.ErrorCode;
import exception.ApiException;

import java.util.Optional;

@Converter
public class BoardDentalTypeConverter {
    public BoardDentalTypeEntity toBoardDentalTypeEntity(BoardEntity board, String dentalType) {
        return BoardDentalTypeEntity.builder()
                .board(board)
                .dentalType(dentalType)
                .build();
    }
}
