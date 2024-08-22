package com.example.domain.reviewDentist.controller;

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
    public ResponseEntity<String> addReview(
            @PathVariable("userId") Long userId,
            @PathVariable("dentistId") String dentistId,
            @RequestBody ReviewRequest reviewRequest
    ){
        return new ResponseEntity<>(reviewBusiness.addReview(userId ,dentistId, reviewRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/review/{dentistId}/{reviewId}")
    public ResponseEntity<String> deleteReview(
            @PathVariable("userId") Long userId,
            @PathVariable("dentistId") String dentistId,
            @PathVariable("reviewId") String reviewId
    ){
        return new ResponseEntity<>(reviewBusiness.deleteReview(userId ,dentistId, reviewId), HttpStatus.OK);
    }

    @PutMapping("/{userId}/review/{dentistId}/{reviewId}")
    public ResponseEntity<String> updateReview(
            @PathVariable("userId") Long userId,
            @PathVariable("dentistId") String dentistId,
            @PathVariable("reviewId") String reviewId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        return new ResponseEntity<>(reviewBusiness.updateReview(userId,dentistId,reviewId,reviewRequest), HttpStatus.OK);
    }

    @GetMapping("/{userId}/review")
    public ResponseEntity<List<ReviewResponse>> findReviewsByUser(
            @PathVariable("userId") Long userId
    ){
        return new ResponseEntity<>(reviewBusiness.findReviewsByUser(userId),HttpStatus.OK);
    }

    @GetMapping("/{userId}/review/{hospitalId}")
    public ResponseEntity<List<ReviewResponse>> findReviewsByUserAndHospital(
            @PathVariable("userId") Long userId,
            @PathVariable("hospitalId") String hospitalId
    ){
        return new ResponseEntity<>(reviewBusiness.findReviewsByUserAndHospital(userId,hospitalId),HttpStatus.OK);
    }

}
