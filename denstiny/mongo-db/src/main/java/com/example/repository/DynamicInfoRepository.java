package com.example.repository;

import com.example.document.DynamicInfoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DynamicInfoRepository extends MongoRepository<DynamicInfoDoc, String> {
}