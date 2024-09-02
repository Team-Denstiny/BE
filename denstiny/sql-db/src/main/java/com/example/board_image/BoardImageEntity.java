package com.example.board;

import com.example.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.domain.Persistable;

@Entity
@Data
@Table(name = "board_image")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardImageEntity extends BaseEntity implements Persistable<String> {
    @Id
    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_size")
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    @Override
    public String getId() {
        return storedFileName;
    }

    @Override
    public boolean isNew() {
        return getCreated_at() == null;
    }
}
