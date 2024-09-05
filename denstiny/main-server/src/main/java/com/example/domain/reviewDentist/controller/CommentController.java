package com.example.domain.reviewDentist.controller;

import api.Api;
import api.Result;
import com.example.domain.reviewDentist.business.CommentBusiness;
import com.example.domain.reviewDentist.controller.model.ReviewRequest;
import com.example.domain.reviewDentist.controller.model.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentBusiness commentBusiness;

    @PostMapping("/user/{userId}/review-comment/{reviewId}")
    public Api<String> addReviewComment(
            @PathVariable("userId") Long userId,
            @PathVariable("reviewId") String reviewId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        String message = commentBusiness.addReplyToComment(userId,reviewId,reviewRequest);
        return new Api<>(new Result(201, "리뷰에 댓글 달기 성공", "성공"), message);
    }

    @GetMapping("/public/review-comment/{reviewId}")
    public Api<List<ReviewResponse>> getReviewComment(
            @PathVariable("reviewId") String reviewId
    ){
        List<ReviewResponse> commentsForReview = commentBusiness.getCommentsForReview(reviewId);
        return new Api<>(new Result(200, "리뷰의 댓글들 가져오기 성공", "성공"), commentsForReview);
    }

    @PutMapping("/user/{userId}/review-comment/{commentId}")
    public Api<ReviewResponse> updateReviewComment(
            @PathVariable("userId") Long userId,
            @PathVariable("commentId") String commentId,
            @RequestBody ReviewRequest reviewRequest
    ){
        ReviewResponse reviewResponse = commentBusiness.updateReplyComment(userId, commentId, reviewRequest);
        return new Api<>(new Result(200, "리뷰의 댓글 수정하기 성공", "성공"), reviewResponse);

    }

    @DeleteMapping("/user/{userId}/review-comment/{commentId}")
    public Api<String> deleteReviewComment(
            @PathVariable("userId") Long userId,
            @PathVariable("commentId") String commentId
    ){
        String message = commentBusiness.deleteReplyComment(userId, commentId);
        return new Api<>(new Result(200, "리뷰의 댓글 삭제하기 성공", "성공"), message);

    }

}
