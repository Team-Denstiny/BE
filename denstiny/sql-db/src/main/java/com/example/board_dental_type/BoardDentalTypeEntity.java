package com.example.board_dental_type;

import com.example.board.BoardEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@Table(name = "board_dental_type")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDentalTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_dental_type_id")
    private Long boardDentalTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Column(name = "dental_type", nullable = false)
    private String dentalType;
}