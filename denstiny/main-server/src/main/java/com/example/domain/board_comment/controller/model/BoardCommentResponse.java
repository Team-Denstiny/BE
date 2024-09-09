package com.example.domain.board_comment.controller.model;

import com.example.board.BoardEntity;
import com.example.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCommentResponse {
    private Long boardCommentId;
    private String content;
    private Long boardId;
    private Long userId;
}
