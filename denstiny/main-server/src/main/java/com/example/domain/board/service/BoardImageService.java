package com.example.domain.board.service;

import com.example.board_image.BoardImageEntity;
import com.example.board_image.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardImageService {
    private final BoardImageRepository boardImageRepository;
    // 게시글 사진 생성
    public BoardImageEntity addBoardImage(BoardImageEntity boardImageEntity) {
        return boardImageRepository.save(boardImageEntity);
    }
}