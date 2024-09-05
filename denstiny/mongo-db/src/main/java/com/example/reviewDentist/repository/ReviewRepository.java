package com.example.reviewDentist.repository;

import com.example.reviewDentist.Document.ReviewDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewDoc,String> {

    // ObjectId로 ReviewDoc 찾기
    boolean existsById(ObjectId id);
    ReviewDoc findById(ObjectId id);
    // 특정 사용자(user_id)로 모든 ReviewDoc을 찾기
    List<ReviewDoc> findByUserId(Long userId);
    // 특정 사용자(user_id)와 hospital_id로 ReviewDoc을 찾기
    List<ReviewDoc> findByUserIdAndHospitalId(Long userId, String hospitalId);

    List<ReviewDoc> findByIdIn(List<ObjectId> ids);

    // 리뷰의 대댓글의 ObjectId를 통해 댓글을 찾는 메서드
    ReviewDoc findFirstByCommentReplysContaining(ObjectId commentId);
}
