package com.example.user_bookmarked.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class UserBookmarkedId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "dentist_id")
    private String dentistId;
}
