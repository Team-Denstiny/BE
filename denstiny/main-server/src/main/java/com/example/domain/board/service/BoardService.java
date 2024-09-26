package com.example.domain.board.service;

import com.example.board.BoardEntity;
import com.example.board.BoardRepository;
import com.example.domain.board.controller.model.BoardGetBoardsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 게시글 프록시 객체 ID 조회
    public BoardEntity getReferenceBoardId(Long boardId) {
        return boardRepository.getReferenceById(boardId);
    }

    // 게시글 삭제
    public void deleteBoard(BoardEntity board) {
        boardRepository.delete(board);
    }

    // 게시글 조회
    public List<BoardEntity> findByWriter(Long writer) { return boardRepository.findByWriter(writer); }

    public Page<BoardEntity> findAll(PageRequest of) {
        return boardRepository.findAll(of);
    }

    public Page<BoardEntity> findByCategory(Long category, PageRequest of) {
        return boardRepository.findByCategory(category, of);
    }

//    // 게시글 프록시 객체 ID 조회 - findById
//    public Optional<BoardEntity> getReferenceBoardId(Long boardId) {
//        return boardRepository.findById(boardId);
//    }

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
