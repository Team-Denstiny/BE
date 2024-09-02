package com.example.domain.board.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.board_image.BoardImageEntity;
import com.example.domain.board.service.BoardImageService;
import com.example.domain.board.service.BoardService;
import com.example.domain.board.converter.BoardConverter;
import com.example.util.ImageUtil;
import com.example.domain.board.controller.model.BoardRequest;
import com.example.domain.board.controller.model.BoardResponse;
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

    public BoardResponse addBoard(BoardRequest boardReq, List<MultipartFile> images, Long userId){

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
}
