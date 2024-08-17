package com.example.repository;

import com.example.document.StaticInfoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticInfoRepository extends MongoRepository<StaticInfoDoc, String> {
}
