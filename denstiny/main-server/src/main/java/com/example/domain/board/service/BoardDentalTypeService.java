package com.example.domain.board.service;

import com.example.board.BoardEntity;
import com.example.board_dental_type.BoardDentalTypeEntity;
import com.example.board_dental_type.BoardDentalTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardDentalTypeService {
    private final BoardDentalTypeRepository boardDentalTypeRepository;

    public void addBoardDentalType(BoardDentalTypeEntity boardDentalTypeEntity) {
        boardDentalTypeRepository.save(boardDentalTypeEntity);
    }
}
