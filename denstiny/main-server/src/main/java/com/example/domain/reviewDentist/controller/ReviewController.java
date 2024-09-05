package com.example.domain.reviewDentist.controller;

import api.Api;
import api.Result;
import com.example.domain.reviewDentist.business.ReviewBusiness;
import com.example.domain.reviewDentist.controller.model.ReviewRequest;
import com.example.domain.reviewDentist.controller.model.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class ReviewController {

    private final ReviewBusiness reviewBusiness;

    @PostMapping("/{userId}/review/{dentistId}")
    public Api<String> addReview(
            @PathVariable("userId") Long userId,
            @PathVariable("dentistId") String dentistId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        String message = reviewBusiness.addReview(userId, dentistId, reviewRequest);
        return new Api<>(new Result(201, "리뷰 추가 성공", "성공"), message);
    }

    @DeleteMapping("/{userId}/review/{dentistId}/{reviewId}")
    public Api<String> deleteReview(
            @PathVariable("userId") Long userId,
            @PathVariable("dentistId") String dentistId,
            @PathVariable("reviewId") String reviewId
    ) {
        String message = reviewBusiness.deleteReview(userId, dentistId, reviewId);
        return new Api<>(new Result(200, "리뷰 삭제 성공", "성공"), message);
    }


    @PutMapping("/{userId}/review/{dentistId}/{reviewId}")
    public Api<String> updateReview(
            @PathVariable("userId") Long userId,
            @PathVariable("dentistId") String dentistId,
            @PathVariable("reviewId") String reviewId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        String message = reviewBusiness.updateReview(userId, dentistId, reviewId, reviewRequest);
        return new Api<>(new Result(200, "리뷰 수정 성공", "성공"), message);
    }


    @GetMapping("/{userId}/review")
    public Api<List<ReviewResponse>> findReviewsByUser(
            @PathVariable("userId") Long userId
    ) {
        List<ReviewResponse> reviews = reviewBusiness.findReviewsByUser(userId);
        return new Api<>(new Result(200, "사용자 리뷰 조회 성공", "성공"), reviews);
    }

    @GetMapping("/{userId}/review/{hospitalId}")
    public Api<List<ReviewResponse>> findReviewsByUserAndHospital(
            @PathVariable("userId") Long userId,
            @PathVariable("hospitalId") String hospitalId
    ) {
        List<ReviewResponse> reviews = reviewBusiness.findReviewsByUserAndHospital(userId, hospitalId);
        return new Api<>(new Result(200, "사용자 및 병원 리뷰 조회 성공", "성공"), reviews);
    }


}
