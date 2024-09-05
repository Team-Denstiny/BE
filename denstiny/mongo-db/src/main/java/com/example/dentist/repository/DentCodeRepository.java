package com.example.dentist.repository;

import com.example.dentist.document.DentCodeDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DentCodeRepository extends MongoRepository<DentCodeDoc, String> {
    @Query("{'kor': {'$regex': ?0}}")
    public List<DentCodeDoc> findByKor(String name);
}
