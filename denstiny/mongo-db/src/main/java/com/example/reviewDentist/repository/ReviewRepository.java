package com.example.reviewDentist.repository;

import com.example.reviewDentist.Document.ReviewDoc;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewDoc,String> {

    // ObjectId로 ReviewDoc 찾기
    boolean existsById(ObjectId id);
    ReviewDoc findById(ObjectId id);
    @Query("{ '_id': ?0, 'depth': 1 }")
    ReviewDoc findByIdAndDepthOne(ObjectId id);

    // 특정 사용자(user_id)로 모든 ReviewDoc을 찾기
    List<ReviewDoc> findByUserId(Long userId);
    // userId와 depth가 1인 ReviewDoc 리스트 찾기
    @Query("{ 'userId': ?0, 'depth': 1 }")
    List<ReviewDoc> findByUserIdAndDepthOne(Long userId);
    // 특정 사용자(user_id)와 hospital_id로 ReviewDoc을 찾기
    List<ReviewDoc> findByUserIdAndHospitalId(Long userId, String hospitalId);

    // userId와 hospitalId가 일치하고 depth가 1인 ReviewDoc 리스트 찾기
    @Query("{ 'userId': ?0, 'hospitalId': ?1, 'depth': 1 }")
    List<ReviewDoc> findByUserIdAndHospitalIdAndDepthOne(Long userId, String hospitalId);

    List<ReviewDoc> findByIdIn(List<ObjectId> ids);

    // 리뷰의 대댓글의 ObjectId를 통해 댓글을 찾는 메서드
    ReviewDoc findFirstByCommentReplysContaining(ObjectId commentId);
}
