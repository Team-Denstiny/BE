package com.example.board_comment;

import com.example.base.BaseEntity;
import com.example.board.BoardEntity;
import com.example.user.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "board_comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCommentEntity extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_comment_id")
    private Long boardCommentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private BoardCommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", orphanRemoval = true)
    private List<BoardCommentEntity> childrenComment = new ArrayList<>();

}