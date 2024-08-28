package com.example.domain.user_bookmarked.controller;

import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.user_bookmarked.business.UserBookmarkedBusiness;
import com.example.domain.user_bookmarked.controller.model.BookmarkRequestDto;
import com.example.domain.user_bookmarked.service.UserBookmarkedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserBookmarkedController {

    private final UserBookmarkedService userBookmarkedService;
    private final UserBookmarkedBusiness userBookmarkedBusiness;

    @PostMapping("/{userId}/bookmark")
    public ResponseEntity<String> addBookmarkedDentist(
            @PathVariable("userId") Long userId,
            @RequestBody BookmarkRequestDto hospitalId
    ){
        userBookmarkedService.addBookmarkedDentist(userId,hospitalId.getHospitalId());
        return ResponseEntity.ok("Hospital ID :" + hospitalId.getHospitalId() + " 가 찜 목록에서 추가되었습니다");
    }
    @DeleteMapping("/{userId}/bookmark/{hospitalId}")
    public ResponseEntity<String>  deleteBookmarkedDentist(
            @PathVariable("userId") Long userId,
            @PathVariable("hospitalId") String hospitalId
    ){
        userBookmarkedService.deleteBookmarkedDentist(userId,hospitalId);
        return ResponseEntity.ok("Hospital ID :" +hospitalId + " 가 찜 목록에서 삭제되었습니다");

    }

    @GetMapping("/{userId}/bookmark")
    public ResponseEntity<List<DentistDto>> deleteBookmarkedDentist(
            @PathVariable("userId") Long userId
    ){
        List<DentistDto> dentistDtos = userBookmarkedBusiness.bookmarkedDentists(userId);
        return ResponseEntity.ok(dentistDtos);
    }

}
