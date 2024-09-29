package com.example.board_comment;

import com.example.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardCommentEntity, Long> {
    List<BoardCommentEntity> findBoardCommentByBoardAndParentCommentIsNull(BoardEntity board);

    List<BoardCommentEntity> findBoardCommentByParentComment(BoardCommentEntity boardComment);

    Long countByBoard(BoardEntity board);
}
