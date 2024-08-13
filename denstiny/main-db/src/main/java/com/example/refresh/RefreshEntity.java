package com.example.refresh;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "refresh")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceId;

    private String refresh;

    private String expiration;
}
