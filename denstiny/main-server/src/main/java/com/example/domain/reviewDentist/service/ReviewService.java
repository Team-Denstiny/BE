package com.example.domain.reviewDentist.service;

import com.example.reviewDentist.Document.ReviewDoc;
import com.example.reviewDentist.repository.ReviewRepository;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<ReviewDoc> findReviewByUserIdAndHospitalId(Long userId, String hospitalId){
        return reviewRepository.findByUserIdAndHospitalId(userId,hospitalId);
    }
    public List<ReviewDoc> findReviewByUserId(Long userId){
        return reviewRepository.findByUserId(userId);
    }

    public ReviewDoc saveReview(ReviewDoc reviewDoc) {
        return reviewRepository.save(reviewDoc);
    }

    public ReviewDoc findReviewByDentistId(String dentistId) {
        return reviewRepository.findById(dentistId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "해당 치과가 없습니다"));
    }
    public ReviewDoc findReviewById(String id) {
        ObjectId objectId = new ObjectId(id);
        return reviewRepository.findById(objectId);
    }

    public void deleteById(String id){
        ObjectId objectId = new ObjectId(id);
        reviewRepository.deleteById(id);
    }

    public boolean isReviewExist(String id) {
        ObjectId objectId = new ObjectId(id);
        return reviewRepository.existsById(objectId);
    }

}
