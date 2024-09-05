package com.example.domain.board.controller.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardGetMyBoardsResponse {
    private Long boardId;
    private String title;
    private String content;
    private Integer category;
    private Integer viewCount;
    private Long writer;
    private Long heartCount;
}
