package com.example.dentist.repository;

import com.example.dentist.document.DentistInfoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DentistInfoRepository extends MongoRepository<DentistInfoDoc,String> {

    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<DentistInfoDoc> findByNameContaining(String name);
}
