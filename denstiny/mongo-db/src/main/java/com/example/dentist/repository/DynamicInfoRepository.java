package com.example.dentist.repository;

import com.example.dentist.document.DynamicInfoDoc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DynamicInfoRepository extends MongoRepository<DynamicInfoDoc, String> {

    @Query("{ 'treat_cate': ?0 }")
    List<DynamicInfoDoc> findByTreatCate(String treatCate);

    // 요일과 시간 조건에 맞는 병원의 정보를 가져오는 쿼리
    @Query("{ 'timeInfo.?0.work_time.0': { $lte: ?1 }, 'timeInfo.?0.work_time.1': { $gte: ?1 }, 'timeInfo.?0.description': { $not: /휴무/ } }")
    List<DynamicInfoDoc> findOpenDentists(String day, String queryTimeStart);
    // 해당 이름이 포함된 모든 병원들의 리스트를 반환
    @Query("{ 'name' : { $regex: ?0, $options: 'i' } }")
    List<DynamicInfoDoc> findByNameContaining(String name);
}

