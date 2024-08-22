package com.example.domain.board.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {
    private String title;
    private String content;
    private List<String> pictures;
}
