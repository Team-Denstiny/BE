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

    @Query("SELECT h.user.userId FROM HeartEntity h WHERE h.board.boardId = :boardId")
    List<Long> findUserIdsByBoardId(@Param("boardId") Long boardId);
}
