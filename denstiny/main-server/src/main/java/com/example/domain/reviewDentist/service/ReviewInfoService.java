package com.example.domain.reviewDentist.service;

import com.example.reviewDentist.Document.ReviewInfoDoc;
import com.example.reviewDentist.repository.ReviewInfoRepository;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewInfoService {

    private final ReviewInfoRepository reviewInfoRepository;
    private final MongoTemplate mongoTemplate;

    public ReviewInfoDoc findReviewInfoDocById(String dentistId){
        return reviewInfoRepository
                .findById(dentistId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));
    }

    public ReviewInfoDoc saveReviewInfoDoc(ReviewInfoDoc reviewInfoDoc){
        return reviewInfoRepository.save(reviewInfoDoc);
    }

    public void deleteReviews(String dentistId, String reviewId){
        List<ObjectId> reviewsIds = findReviewInfoDocById(dentistId).getReviews();
        ObjectId objectReviewId = new ObjectId(reviewId);
        if (reviewsIds.contains(objectReviewId)){
            reviewsIds.remove(objectReviewId);

            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("_id").is(dentistId)),
                    new Update().pull("reviews", objectReviewId),
                    ReviewInfoDoc.class
            );

        } else {
            throw new ApiException(ErrorCode.NULL_POINT,"reviews에 해당 id의 reivew가 없습니다");
        }
    }
}
