package com.example.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Page<BoardEntity> findByWriter(Long writer, PageRequest of);

    Page<BoardEntity> findByCategory(Long category, PageRequest of);
}
