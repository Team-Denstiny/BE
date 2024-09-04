package com.example.domain.board.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.board_image.BoardImageEntity;
import com.example.domain.board.service.BoardImageService;
import com.example.domain.board.service.BoardService;
import com.example.domain.board.converter.BoardConverter;
import com.example.util.ImageUtil;
import com.example.domain.board.controller.model.BoardAddRequest;
import com.example.domain.board.controller.model.BoardAddResponse;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;


@Business
@AllArgsConstructor
public class BoardBusiness {
    private final BoardService boardService;
    private final BoardImageService boardImageService;
    private final BoardConverter boardConverter;

    public BoardAddResponse addBoard(BoardAddRequest boardReq, List<MultipartFile> images, Long userId){
        BoardEntity board = Optional.ofNullable(boardReq)
                .map(it -> boardConverter.toBoardEntity(it, userId))
                .map(it -> boardService.addBoard(it))
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "addBoard null point"));

        // findById 시 Optional 처리
        // BoardEntity boardId = boardService.getReferenceBoardId(board.getBoardId()).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "Board Id null point"));
        try {
            List<BoardImageEntity> list = ImageUtil.parseFileInfo(boardService.getReferenceBoardId(board.getBoardId()), images);

            if (list.isEmpty()){
            }
            else{
                List<BoardImageEntity> pictureBeans = new ArrayList<>();
                for (BoardImageEntity boardImage : list) {
                    pictureBeans.add(boardImageService.addBoardImage(boardImage));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return boardConverter.toBoardResponse(board);
    }


    public String deleteBoard(Long userId, Long boardId) {
        // 1. getReferenceBoardId로 Board 엔티티 가져옴
        BoardEntity board = boardService.getReferenceBoardId(boardId);

        // 2. 조회한 board의 writer가 userId와 같은지 비교
        if(board.getWriter().equals(userId)) {
            // 3. writer와 userId가 같으면 게시글 삭제
            boardService.deleteBoard(board);
            return "성공적으로 유저" + userId + "이 게시글 번호" + boardId + "게시글을 삭제하였습니다.";
        }
        return "유저" + userId + "이 게시글 번호" + boardId + "게시글 삭제에 실패하였습니다.";
    }
}
