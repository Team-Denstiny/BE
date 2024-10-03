package com.example.domain.heart.service;

import com.example.board.BoardEntity;
import com.example.heart.HeartEntity;
import com.example.heart.HeartRepository;
import com.example.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;

    // User와 Board로 좋아요 조회
    public Optional<HeartEntity> findByUserAndBoard(UserEntity user, BoardEntity board) {
        return heartRepository.findByUserAndBoard(user, board);
    }

    // userId로 좋아요 프록시 객체 조회
    public List<HeartEntity> findByUser(UserEntity user) { return heartRepository.findByUser(user); }

    // 좋아요 생성
    public HeartEntity addHeart(HeartEntity heartEntity) {
        return heartRepository.save(heartEntity);
    }

    // 좋아요 삭제
    public void deleteHeart(HeartEntity heart) {
        heartRepository.delete(heart);
    }

    // 로그인한 유저가 좋아요한 게시글인지 아닌지 확인
//    public Boolean isUserLikedBoard(Long boardId, Long userId) {
//        return heartRepository.existsByBoard_BoardIdAndUser_UserId(boardId, userId);
//    }
}
