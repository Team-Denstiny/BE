package com.example.domain.board.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {
    private String title;
    private String content;
    private Integer category;
}
