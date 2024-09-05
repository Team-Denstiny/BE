package com.example.reviewDentist.repository;

import com.example.reviewDentist.Document.ReviewInfoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewInfoRepository extends MongoRepository<ReviewInfoDoc,String> {
}
