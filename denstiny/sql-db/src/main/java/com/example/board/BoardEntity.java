package com.example.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "category", nullable = false)
    private Integer category;

    @ColumnDefault("0")
    @Column(name = "view_count",nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "writer", nullable = false)
    private Long writer;

    // 다대일 양방향 참조 필요시 사용
//    @OneToMany(mappedBy = "board")
//    private List<BoardImageEntity> boardImages = new ArrayList<>();
}
