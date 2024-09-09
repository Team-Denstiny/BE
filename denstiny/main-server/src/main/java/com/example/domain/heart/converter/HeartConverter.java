package com.example.domain.heart.converter;

import annotation.Converter;
import com.example.domain.heart.controller.model.HeartResponse;
import com.example.heart.HeartEntity;
import com.example.user.UserEntity;
import com.example.board.BoardEntity;
import error.ErrorCode;
import exception.ApiException;

import java.util.Optional;


@Converter
public class HeartConverter {

    public HeartEntity toHeartEntity(UserEntity user, BoardEntity board) {
        return Optional.ofNullable(
                HeartEntity.builder()
                        .user(user)
                        .board(board)
                        .build()
        )
        .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "user or board is null"));
    }

    public HeartResponse toHeartResponse(HeartEntity heart) {
        return Optional.ofNullable(heart)
                .map(it -> {
                    return HeartResponse.builder()
                            .heartId(it.getHeartId())
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "HeartEntity is null"));
    }
}