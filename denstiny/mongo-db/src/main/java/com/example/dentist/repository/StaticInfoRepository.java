package com.example.dentist.repository;

import com.example.dentist.document.StaticInfoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticInfoRepository extends MongoRepository<StaticInfoDoc, String> {
}
