package com.example.domain.reviewDentist.controller;

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
    public ResponseEntity<List<ReviewResponse>> findReviewsByHospitalId(
            @PathVariable("hospitalId") String hospitalId
    ){
        return new ResponseEntity<>(reviewBusiness.findReviewsByDentist(hospitalId), HttpStatus.OK);
    }
}
