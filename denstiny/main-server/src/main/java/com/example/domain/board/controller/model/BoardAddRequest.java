package com.example.domain.board.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardAddRequest {
    private String title;
    private String content;
    private Integer category;
    private Optional<List<String>> dentalType;
}
