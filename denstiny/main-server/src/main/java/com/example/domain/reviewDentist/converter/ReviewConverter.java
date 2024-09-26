package com.example.domain.reviewDentist.converter;

import annotation.Converter;
import com.example.domain.reviewDentist.controller.model.ReviewResponse;
import com.example.domain.reviewDentist.controller.model.ReviewRequest;
import com.example.reviewDentist.Document.ReviewDoc;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Converter
@AllArgsConstructor
public class ReviewConverter {

    public ReviewDoc toReviewDoc(ReviewRequest reviewRequest){
        return ReviewDoc.builder()
                .date(LocalDateTime.now())
                .userId(reviewRequest.getUserId())
                .content(reviewRequest.getContent())
                .build();
    }

    public ReviewResponse toReviewResponse(ReviewDoc reviewDoc){
        return ReviewResponse.builder()
                .id(reviewDoc.getId())
                .userId(reviewDoc.getUserId())
                .date(reviewDoc.getDate())
                .hospitalId(reviewDoc.getHospitalId())
                .content(reviewDoc.getContent())
                .build();
    }
}
