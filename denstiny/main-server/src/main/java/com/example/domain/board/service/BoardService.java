package com.example.domain.board.service;

import com.example.board.BoardEntity;
import com.example.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 생성
    public BoardEntity addBoard(BoardEntity boardEntity) {
        return boardRepository.save(boardEntity);
    }

//    // 게시글 수정
//    public Optional<BoardEntity> updateBoard(Long id, BoardEntity updatedBoard) {
//        return boardRepository.findById(id).map(board -> {
//            board.setTitle(updatedBoard.getTitle());
//            board.setContent(updatedBoard.getContent());
//            board.setViewCount(updatedBoard.getViewCount());
//            return boardRepository.save(board);
//        });
//    }
//
//    // 게시글 삭제
//    public void deleteBoard(Long id) {
//        boardRepository.deleteById(id);
//    }
}
