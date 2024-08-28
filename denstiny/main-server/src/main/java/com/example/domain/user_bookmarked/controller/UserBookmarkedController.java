package com.example.domain.user_bookmarked.controller;

import api.Api;
import api.Result;
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
    public Api<String> addBookmarkedDentist(
            @PathVariable("userId") Long userId,
            @RequestBody BookmarkRequestDto hospitalId
    ) {
        userBookmarkedService.addBookmarkedDentist(userId, hospitalId.getHospitalId());

        String message = "Hospital ID :" + hospitalId.getHospitalId() + " 가 찜 목록에서 추가되었습니다";
        return new Api<>(new Result(200, "찜 목록에 추가 성공", "성공"), message);
    }
    @DeleteMapping("/{userId}/bookmark/{hospitalId}")
    public Api<String> deleteBookmarkedDentist(
            @PathVariable("userId") Long userId,
            @PathVariable("hospitalId") String hospitalId
    ) {
        userBookmarkedService.deleteBookmarkedDentist(userId, hospitalId);

        String message = "Hospital ID :" + hospitalId + " 가 찜 목록에서 삭제되었습니다";
        return new Api<>(new Result(200, "찜 목록에서 삭제 성공", "성공"), message);
    }

    @GetMapping("/{userId}/bookmark")
    public Api<List<DentistDto>> getBookmarkedDentists(
            @PathVariable("userId") Long userId
    ) {
        List<DentistDto> dentistDtos = userBookmarkedBusiness.bookmarkedDentists(userId);
        return new Api<>(new Result(200, "찜 목록 조회 성공", "성공"), dentistDtos);
    }

}
