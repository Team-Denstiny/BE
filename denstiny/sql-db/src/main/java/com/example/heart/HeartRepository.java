package com.example.heart;

import com.example.board.BoardEntity;
import com.example.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface HeartRepository extends JpaRepository<HeartEntity, Long> {
    Optional<HeartEntity> findByUserAndBoard(UserEntity user, BoardEntity board);

    List<HeartEntity> findByUser(UserEntity user);

    // 리팩토링 전 로그인 한 유저가 게시판에 좋아요를 눌렀는지 확인하는 쿼리
    // @Query("SELECT COUNT(h) > 0 FROM HeartEntity h WHERE h.board.boardId = :boardId AND h.user.userId = :userId")
    // 무거운 Count 쿼리 대신, EXISTS를 활용하여 True, False 반환하도록 쿼리 개선
    // 에러: JPQL에서 아래 쿼리 처리에서 에러 발생
    //    @Query("SELECT EXISTS (SELECT 1 FROM HeartEntity h WHERE h.board.boardId = :boardId AND h.user.userId = :userId")
    //    Boolean existsByBoardIdAndUserId(@Param("boardId") Long boardId, @Param("userId") Long userId);
    Boolean existsByBoard_BoardIdAndUser_UserId(Long boardId, Long userId);
}
