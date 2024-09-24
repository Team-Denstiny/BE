package com.example.domain.board_comment.service;

import com.example.board.BoardEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board_comment.BoardCommentEntity;
import com.example.board_comment.BoardCommentRepository;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public BoardCommentEntity saveBoardComment(BoardCommentEntity boardCommentEntity){
        return boardCommentRepository.save(boardCommentEntity);
    }

    public boolean isBoardCommentExist(Long boardCommentId) {
        return boardCommentRepository.existsById(boardCommentId);
    }

    public BoardCommentEntity getReferenceById(Long boardCommentId) {
        return boardCommentRepository.getReferenceById(boardCommentId);
    }

    public void deleteById(Long boardCommentId) {
        boardCommentRepository.deleteById(boardCommentId);
    }

    public List<BoardCommentEntity> findBoardCommentByBoardAndParentCommentIsNull(BoardEntity board) {
        return boardCommentRepository.findBoardCommentByBoardAndParentCommentIsNull(board);
    }

    public List<BoardCommentEntity> findBoardCommentByParentComment(BoardCommentEntity boardComment) {
        return boardCommentRepository.findBoardCommentByParentComment(boardComment);
    }
}
