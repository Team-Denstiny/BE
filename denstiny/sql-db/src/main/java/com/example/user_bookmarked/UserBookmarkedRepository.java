package com.example.user_bookmarked;

import com.example.user_bookmarked.embedded.UserBookmarkedId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBookmarkedRepository extends JpaRepository<UserBookmarkedEntity, UserBookmarkedId> {

    List<UserBookmarkedEntity> findById_UserId(Long userId);
}
