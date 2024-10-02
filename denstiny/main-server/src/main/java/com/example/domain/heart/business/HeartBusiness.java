package com.example.domain.heart.business;

import annotation.Business;
import com.example.board.BoardEntity;
import com.example.domain.board.service.BoardImageService;
import com.example.domain.board.service.BoardService;
import com.example.domain.heart.controller.model.HeartRequest;
import com.example.domain.heart.controller.model.HeartResponse;
import com.example.domain.heart.converter.HeartConverter;
import com.example.domain.heart.service.HeartService;
import com.example.domain.user.service.UserService;
import com.example.heart.HeartEntity;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Business
@AllArgsConstructor
public class HeartBusiness {
    private final UserService userService;
    private final BoardService boardService;
    private final HeartService heartService;
    private final BoardImageService boardImageService;
    private final HeartConverter heartConverter;

    // 좋아요 생성
    @Transactional
    public HeartResponse addHeart(HeartRequest heartReq, Long userId) {
        // 1. getReferecneUserId로 User 엔티티 가져옴
        UserEntity user = userService.getReferenceUserId(userId);

        // 2. getReferenceBoardId로 Board 엔티티 가져옴
        BoardEntity board = boardService.getReferenceBoardId(heartReq.getBoardId());

        // 3. 조회한 user와 board의 프록시 객체로 조회
        if(heartService.findByUserAndBoard(user, board).isPresent()){
            throw new ApiException(ErrorCode.BAD_REQUEST, "이미 좋아요가 되어있어요.");
        }

        // 4. 게시글의 heart_count 값 +1
        board.setHeartCount(board.getHeartCount() + 1);
        boardService.updateBoard(board);

        return Optional.ofNullable(heartConverter.toHeartEntity(user, board))
                .map(it -> heartService.addHeart(it))
                .map(it -> heartConverter.toHeartResponse(it))
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "Heart is null"));
    }

    // 좋아요 삭제
    @Transactional
    public String deleteHeart(HeartRequest heartReq, Long userId) {
        // getReferecneUserId로 User 엔티티 가져옴
        UserEntity user = userService.getReferenceUserId(userId);

        // getReferenceBoardId로 Board 엔티티 가져옴
        BoardEntity board = boardService.getReferenceBoardId(heartReq.getBoardId());

        // 조회한 user와 board의 프록시 객체로 조회
        HeartEntity heart = heartService.findByUserAndBoard(user, board)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "Heart is null"));

        // 게시글의 heart_count 값 -1
        board.setHeartCount(board.getHeartCount() - 1);
        boardService.updateBoard(board);

        // 존재하면, 해당 좋아요 삭제
        heartService.deleteHeart(heart);
        return "성공적으로 유저" + heart.getUser().getUserId() + "이 게시글 번호" + heart.getBoard().getBoardId() + " 게시글의 좋아요를 삭제하였습니다.";
    }
}
