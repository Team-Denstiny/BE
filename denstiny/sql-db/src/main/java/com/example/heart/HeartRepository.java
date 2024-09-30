package com.example.heart;

import com.example.board.BoardEntity;
import com.example.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface HeartRepository extends JpaRepository<HeartEntity, Long> {
    Optional<HeartEntity> findByUserAndBoard(UserEntity user, BoardEntity board);

    List<HeartEntity> findByUser(UserEntity user);
    Long countByBoard(BoardEntity board);

    @Query("SELECT COUNT(h) > 0 FROM HeartEntity h WHERE h.board.boardId = :boardId AND h.user.userId = :userId")
    Boolean existsByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") Long userId);
}
