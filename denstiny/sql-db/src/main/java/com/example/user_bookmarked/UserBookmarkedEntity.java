package com.example.user_bookmarked;

import com.example.user.UserEntity;
import com.example.user_bookmarked.embedded.UserBookmarkedId;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;

@Data
@Table(name = "user_bookmarked_dentists")
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserBookmarkedEntity {

    @EmbeddedId
    private UserBookmarkedId id;

}
