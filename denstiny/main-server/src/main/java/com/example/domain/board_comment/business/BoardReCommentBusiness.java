package com.example.domain.board_comment.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.board_comment.BoardCommentEntity;
import com.example.domain.board.service.BoardService;
import com.example.domain.board_comment.controller.model.BoardReCommentAddRequest;
import com.example.domain.board_comment.converter.BoardReCommentConverter;
import com.example.domain.board_comment.service.BoardCommentService;
import com.example.domain.user.service.UserService;
import com.example.error.UserErrorCode;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
@Transactional
public class BoardReCommentBusiness {
    private final BoardCommentService boardCommentService;
    private final BoardReCommentConverter boardReCommentConverter;
    private final UserService userService;
    private final BoardService boardService;
    public String addBoardReComment(Long userId, Long boardId, Long commentId, BoardReCommentAddRequest req){
        BoardCommentEntity boardComment = boardCommentService.getReferenceById(commentId);

        if(boardComment == null) {
            throw new ApiException(ErrorCode.NULL_POINT, commentId + "번 댓글을 찾을 수 없습니다.");
        }

        UserEntity user = userService.getReferenceUserId(userId);
        BoardEntity board = boardService.getReferenceBoardId(boardId);

        BoardCommentEntity reBoardComment = boardReCommentConverter.toBoardReCommentEntity(req);
        reBoardComment.setUser(user);
        reBoardComment.setBoard(board);
        reBoardComment.setParentComment(boardComment);

        boardCommentService.saveBoardComment(reBoardComment);

        return boardId + "게시글의 " + commentId + "댓글에 대댓글이 추가되었습니다.";
    }

    public String deleteBoardReComment(Long userId, Long boardId, Long boardCommentId, Long boardReCommentId) {
        if(!boardCommentService.isBoardCommentExist(boardReCommentId)){
            throw new ApiException(ErrorCode.NULL_POINT, "해당 대댓글은 없는 대댓글입니다.");
        }

        BoardCommentEntity reBoardComment = boardCommentService.getReferenceById(boardReCommentId);

        if(!reBoardComment.getUser().getUserId().equals(userId)) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자신이 작성한 대댓글만 삭제할 수 있습니다.");
        }

        if(!reBoardComment.getParentComment().getBoardCommentId().equals(boardCommentId)) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "대댓글의 부모 댓글이 다릅니다.");
        }

        boardCommentService.deleteById(boardReCommentId);

        return boardId + "번 게시글의 " + boardReCommentId + "번 대댓글이 성공적으로 삭제되었습니다.";
    }

    public String updateBoardReComment(Long userId, Long boardId, Long boardCommentId, Long boardReCommentId, BoardReCommentAddRequest req) {

        if(!boardCommentService.isBoardCommentExist(boardReCommentId)){
            throw new ApiException(ErrorCode.NULL_POINT, "해당 대댓글은 없는 대댓글입니다.");
        }

        BoardCommentEntity reBoardComment = boardCommentService.getReferenceById(boardReCommentId);

        if(!reBoardComment.getUser().getUserId().equals(userId)) {
            throw new ApiException(UserErrorCode.USER_NOT_AUTHORIZED, "자신이 작성한 대댓글만 수정할 수 있습니다.");
        }
        if(!reBoardComment.getParentComment().getBoardCommentId().equals(boardCommentId)) {
            throw new ApiException(ErrorCode.SERVER_ERROR, "대댓글의 부모 댓글이 다릅니다.");
        }

        reBoardComment.setContent(req.getContent());

        boardCommentService.saveBoardComment(reBoardComment);

        return boardId + "번 게시글의 " + boardReCommentId + "번 대댓글이 성공적으로 수정되었습니다.";
    }

}
