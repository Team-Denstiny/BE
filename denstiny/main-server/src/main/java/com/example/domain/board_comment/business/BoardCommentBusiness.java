package com.example.domain.board_comment.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.board_comment.BoardCommentEntity;
import com.example.domain.board.service.BoardService;
import com.example.domain.board_comment.controller.model.BoardCommentAddRequest;
import com.example.domain.board_comment.converter.BoardCommentConverter;
import com.example.domain.board_comment.service.BoardCommentService;
import com.example.domain.user.service.UserService;
import com.example.user.UserEntity;
import lombok.AllArgsConstructor;

@Business
@AllArgsConstructor
public class BoardCommentBusiness {
    private UserService userService;
    private BoardService boardService;
    private BoardCommentService boardCommentService;
    private BoardCommentConverter boardCommentConverter;

    public String addBoardComment(Long userId, Long boardId, BoardCommentAddRequest req) {
        UserEntity user = userService.getReferenceUserId(userId);
        BoardEntity board = boardService.getReferenceBoardId(boardId);

        BoardCommentEntity boardCommentEntity = boardCommentConverter.toBoardCommentEntity(req);
        boardCommentEntity.setUser(user);
        boardCommentEntity.setBoard(board);
        boardCommentService.saveBoardComment(boardCommentEntity);

        return boardId + "에 댓글이 추가되었습니다.";
    }
}
