package com.example.domain.heart.service;

import com.example.board.BoardEntity;
import com.example.heart.HeartEntity;
import com.example.heart.HeartRepository;
import com.example.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;

    public Optional<HeartEntity> findByUserAndBoard(UserEntity user, BoardEntity board) {
        return heartRepository.findByUserAndBoard(user, board);
    }

    // 좋아요 생성
    public HeartEntity addHeart(HeartEntity heartEntity) {
        return heartRepository.save(heartEntity);
    }

    // 좋아요 삭제
    public void deleteHeart(HeartEntity heart) {
        heartRepository.delete(heart);
    }

    // 좋아요 작성자 총 갯수
    public Long countByBoard(BoardEntity board) { return heartRepository.countByBoard(board); }
}
