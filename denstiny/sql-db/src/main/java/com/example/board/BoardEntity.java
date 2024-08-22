package com.example.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ColumnDefault("0")
    @Builder.Default
    @Column(name = "view_count",nullable = false)
    private Integer viewCount = 0;

    @Column(name = "writer", nullable = false)
    private Long writer;
}
