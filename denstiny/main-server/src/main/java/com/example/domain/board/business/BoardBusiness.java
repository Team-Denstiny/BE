package com.example.domain.board.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.board_dental_type.BoardDentalTypeEntity;
import com.example.board_image.BoardImageEntity;
import com.example.domain.board.controller.model.BoardGetMyBoardsResponse;
import com.example.domain.board.converter.BoardDentalTypeConverter;
import com.example.domain.board.service.BoardDentalTypeService;
import com.example.domain.board.service.BoardImageService;
import com.example.domain.board.service.BoardService;
import com.example.domain.board.converter.BoardConverter;
import com.example.domain.heart.controller.model.HeartResponse;
import com.example.domain.heart.service.HeartService;
import com.example.util.BoardImageUtil;
import com.example.domain.board.controller.model.BoardAddRequest;
import com.example.domain.board.controller.model.BoardAddResponse;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


@Business
@AllArgsConstructor
public class BoardBusiness {
    private final BoardService boardService;
    private final BoardImageService boardImageService;
    private final BoardConverter boardConverter;
    private final BoardDentalTypeService boardDentalTypeService;
    private final BoardDentalTypeConverter boardDentalTypeConverter;
    private final HeartService heartService;

    // 게시글 생성
    public BoardAddResponse addBoard(BoardAddRequest boardReq, List<MultipartFile> images, Long userId){
        BoardEntity board = Optional.ofNullable(boardReq)
                .map(it -> boardConverter.toBoardEntity(it, userId))
                .map(it -> boardService.addBoard(it))
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "addBoard null point"));

        // findById 시 Optional 처리
        // BoardEntity boardId = boardService.getReferenceBoardId(board.getBoardId()).orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "Board Id null point"));
        try {
            List<BoardImageEntity> list = BoardImageUtil.parseFileInfo(boardService.getReferenceBoardId(board.getBoardId()), images);

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

//        if(boardReq.getCategory() == 1 && ObjectUtils.isEmpty(boardReq.getDentalType())) {
//            Optional.ofNullable(boardReq.getDentalType())
//                    .map(it -> boardDentalTypeConverter.toBoardDentalTypeEntity(boardService.getReferenceBoardId(board.getBoardId()), it))
//                    .map(it -> boardDentalTypeService.addBoardDentalType(it))
//                    .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "Dental Type is null"));
//            }
//        }

        return boardConverter.toBoardResponse(board);
    }

    // 게시글 삭제
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

    // 게시글 조회 (내가 쓴 글)
    public List<BoardGetMyBoardsResponse> getMyBoards(Long userId) {
        List<BoardEntity> myBoards = boardService.getMyBoards(userId);

//        for(BoardEntity myBoard: myBoards) {
//              myBoardsRes.add(BoardGetMyBoardsResponse.builder()
//                      .boardId(myBoard.getBoardId())
//                      .title(myBoard.getTitle())
//                      .content(myBoard.getContent())
//                      .category(myBoard.getCategory())
//                      .viewCount(myBoard.getViewCount())
//                      .writer(myBoard.getWriter())
//                      .heartCount(heartService.countByBoard(myBoard))
//                      .build());
//        }

        return myBoards.stream()
                .map(myBoard -> {
                    BoardGetMyBoardsResponse boardGetMyBoardsResponse = boardConverter.toBoardGetMyBoardsResponse(myBoard);
                    boardGetMyBoardsResponse.setHeartCount(heartService.countByBoard(myBoard));
                    return boardGetMyBoardsResponse;
                }).collect(Collectors.toList());
    }
}
