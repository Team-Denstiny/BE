package com.example.domain.board_comment.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.board_comment.BoardCommentEntity;
import com.example.domain.board.service.BoardService;
import com.example.domain.board_comment.controller.model.BoardCommentAddRequest;
import com.example.domain.board_comment.controller.model.BoardCommentResponse;
import com.example.domain.board_comment.converter.BoardCommentConverter;
import com.example.domain.board_comment.service.BoardCommentService;
import com.example.domain.user.service.UserService;
import com.example.error.UserErrorCode;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
        boardCommentEntity.setParentComment(null);

        boardCommentService.saveBoardComment(boardCommentEntity);

        return boardId + "게시글에 댓글이 추가되었습니다.";
    }

    public String deleteBoardComment(Long userId, Long boardId, Long boardCommentId) {
        if(!boardCommentService.isBoardCommentExist(boardCommentId)){
            throw new ApiException(ErrorCode.NULL_POINT, "해당 댓글은 없는 댓글입니다.");
        }

        BoardCommentEntity boardCommentEntity = boardCommentService.getReferenceById(boardCommentId);

        if(!boardCommentEntity.getUser().getUserId().equals(userId)) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자신이 작성한 댓글만 삭제할 수 있습니다.");
        }

        boardCommentService.deleteById(boardCommentId);

        return boardId + "번 게시글의 " + boardCommentId + "번 댓글이 성공적으로 삭제되었습니다.";
    }

    public String updateBoardComment(Long userId, Long boardId, Long boardCommentId, BoardCommentAddRequest req) {
        if(!boardCommentService.isBoardCommentExist(boardCommentId)){
            throw new ApiException(ErrorCode.NULL_POINT, "해당 댓글은 없는 댓글입니다.");
        }

        BoardCommentEntity boardCommentEntity = boardCommentService.getReferenceById(boardCommentId);

        if(!boardCommentEntity.getUser().getUserId().equals(userId)) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자신이 작성한 댓글만 수정할 수 있습니다.");
        }

        boardCommentEntity.setContent(req.getContent());

        boardCommentService.saveBoardComment(boardCommentEntity);

        return boardId + "번 게시글의 " + boardCommentId + "번 댓글이 성공적으로 수정되었습니다.";
    }

    public List<BoardCommentResponse> findBoardCommentsByBoard(Long userId, Long boardId) {
        BoardEntity board = boardService.getReferenceBoardId(boardId);
        return boardCommentService.findBoardCommentByBoardAndParentCommentIsNull(board).stream()
                .map(boardComment -> {
                    BoardCommentResponse boardCommentResponse = boardCommentConverter.toBoardCommentResponse(boardComment);
                    boardCommentResponse.setChildrenComment(
                            boardCommentService.findBoardCommentByParentComment(boardComment).stream()
                                    .map(boardReComment -> {
                                        return boardCommentConverter.toBoardCommentResponse(boardReComment);
                                    }).collect(Collectors.toList()));
                    return boardCommentResponse;
                })
                .collect(Collectors.toList());
    }
}
