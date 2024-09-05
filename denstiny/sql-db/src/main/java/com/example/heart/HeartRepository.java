package com.example.heart;

import com.example.board.BoardEntity;
import com.example.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<HeartEntity, Long> {
    Optional<HeartEntity> findByUserAndBoard(UserEntity user, BoardEntity board);

    Long countByBoard(BoardEntity board);
}
