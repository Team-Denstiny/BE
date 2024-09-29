package com.example.domain.board.converter;

import annotation.Converter;

import java.util.List;
import java.util.Optional;

import com.example.board.BoardEntity;
import com.example.domain.board.controller.model.BoardAddRequest;
import com.example.domain.board.controller.model.BoardAddResponse;
import com.example.domain.board.controller.model.BoardGetBoardsResponse;
import com.example.domain.board.service.BoardService;
import com.example.domain.board_comment.service.BoardCommentService;
import com.example.domain.heart.service.HeartService;
import com.example.domain.user.service.UserService;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Converter
public class BoardConverter {

    private final UserService userService;
    private final HeartService heartService;
    private final BoardCommentService boardCommentService;
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

    public BoardAddResponse toBoardAddResponse(BoardEntity board){
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
    public BoardGetBoardsResponse toBoardGetMyBoardsResponse2(BoardEntity board){
        return Optional.ofNullable(board)
                .map(entity -> {
                    Long writer = entity.getWriter();
                    UserEntity user = userService.getUserById(writer);

                    return BoardGetBoardsResponse.builder()
                            .boardId(entity.getBoardId())
                            .title(entity.getTitle())
                            .content(entity.getContent())
                            .category(entity.getCategory())
                            .viewCount(entity.getViewCount())
                            .createdAt(entity.getCreated_at())
                            .updatedAt(entity.getUpdated_at())
                            .heartCount(heartService.countByBoard(entity))
                            .replyCount(boardCommentService.countBoardReply(entity))
                            .writer(writer)
                            .profileImg(user.getProfileImg())
                            .writerNickname(user.getNickName())
                            .imgs(List.of("https://github.com/user-attachments/assets/8734c604-5fc8-4317-8f8f-f430cd564c1e"))
                            .build();
                }).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "BoardEntity is null"));
    }
}