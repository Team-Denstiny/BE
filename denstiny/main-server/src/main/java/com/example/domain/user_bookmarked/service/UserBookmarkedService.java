package com.example.domain.user_bookmarked.service;

import com.example.user_bookmarked.UserBookmarkedEntity;
import com.example.user_bookmarked.UserBookmarkedRepository;
import com.example.user_bookmarked.embedded.UserBookmarkedId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBookmarkedService {

    private final UserBookmarkedRepository userBookmarkedRepository;

    public void addBookmarkedDentist(Long userId, String dentistId){
        UserBookmarkedEntity userBookmarkedEntity = UserBookmarkedEntity.builder()
                .id(new UserBookmarkedId(userId, dentistId))
                .build();
        userBookmarkedRepository.save(userBookmarkedEntity);
    }

    public void deleteBookmarkedDentist(Long userId, String dentistId){
        userBookmarkedRepository.deleteById(new UserBookmarkedId(userId, dentistId));
    }

    public List<String> readBookmarkedDentists(Long userId){
        return userBookmarkedRepository.findById_UserId(userId)
                .stream()
                .map(entity -> {
                    return entity.getId().getDentistId();
                }).collect(Collectors.toList());
    }
}
