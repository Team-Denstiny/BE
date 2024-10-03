package com.example.domain.board.controller.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardGetBoardsResponse {
    private Long boardId;
    private String title;
    private String content;
    private Integer category;
    private Integer viewCount;
    private Long writer;
    private Integer heartCount;
//    private Boolean heartUser;
    private Integer commentCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String profileImg;
    private String writerNickname;
    private List<String> imgs = new ArrayList<>();
}
