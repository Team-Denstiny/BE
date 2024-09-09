package com.example.domain.reviewDentist.controller;

import api.Api;
import api.Result;
import com.example.domain.reviewDentist.business.ReviewBusiness;
import com.example.domain.reviewDentist.controller.model.ReviewResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/public")
public class PublicReviewController {

    private final ReviewBusiness reviewBusiness;

    @GetMapping("/review/{hospitalId}")
    public Api<List<ReviewResponse>> findReviewsByHospitalId(
            @PathVariable("hospitalId") String hospitalId
    ) {
        List<ReviewResponse> reviews = reviewBusiness.findReviewsByDentist(hospitalId);
        return new Api<>(new Result(200, "병원 id : " + hospitalId+" 에 대한 리뷰 조회 성공", "성공"), reviews);
    }
}
