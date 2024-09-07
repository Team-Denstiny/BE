package com.example.domain.board_comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.board_comment.BoardCommentEntity;
import com.example.board_comment.BoardCommentRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public BoardCommentEntity saveBoardComment(BoardCommentEntity boardCommentEntity){
        return boardCommentRepository.save(boardCommentEntity);
    }
}
