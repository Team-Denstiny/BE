package com.example.domain.dentist.service;

import com.example.dentist.document.DentistInfoDoc;
import com.example.dentist.document.DynamicInfoDoc;
import com.example.dentist.document.StaticInfoDoc;
import com.example.dentist.repository.DentistInfoRepository;
import com.example.dentist.repository.DynamicInfoRepository;
import com.example.dentist.repository.StaticInfoRepository;
import com.example.domain.dentist.controller.model.SearchNameDto;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final DentistInfoRepository dentistInfoRepository;
    private final MongoTemplate mongoTemplate;

    public DynamicInfoDoc saveDynamicInfo(DynamicInfoDoc dynamicInfoDoc){
        return dynamicInfoRepository.save(dynamicInfoDoc);
    }
    public DynamicInfoDoc findDynamicInfoById(String dentistId){
        return dynamicInfoRepository
                .findById(dentistId)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));
    }
    public void deleteReviews(String dentistId, String reviewId){
        List<ObjectId> reivewIds = findDynamicInfoById(dentistId).getReviews();
        ObjectId objectReviewId = new ObjectId(reviewId);

        if (reivewIds.contains(objectReviewId)){
            reivewIds.remove(objectReviewId);
        } else {
            throw new ApiException(ErrorCode.NULL_POINT,"reviews에 해당 id의 reivew가 없습니다");
        }
    }

    public DentistInfoDoc findDentistInfoById(String id){
        return dentistInfoRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));
    }

    public List<DentistInfoDoc> findByNameContaining(SearchNameDto searchNameDto,String lastDentistId, int limit) {
        // DTO에서 이름, 위도, 경도 추출
        String name = searchNameDto.getName();
        double longitude = searchNameDto.getLongitude();
        double latitude = searchNameDto.getLatitude();

        Query query = new Query();

        query.addCriteria(Criteria.where("name").regex(name, "i")); // 대소문자 구분 없이 검색
        query.addCriteria(Criteria.where("location")
                .nearSphere(new Point(longitude, latitude)));

        // 쿼리를 실행하고 결과 반환
        List<DentistInfoDoc> allDentists = mongoTemplate.find(query, DentistInfoDoc.class);

        int startIndex = 0;
        if (lastDentistId != null) {
            for (int i = 0; i < allDentists.size(); i++) {
                if (allDentists.get(i).getId().equals(lastDentistId)) {
                    startIndex = i + 1; // 다음 병원부터 시작
                    break;
                }
            }
        }

        return allDentists.stream()
                .skip(startIndex)
                .limit(limit)
                .collect(Collectors.toList());

    }
}
