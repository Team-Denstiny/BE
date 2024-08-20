package com.example.domain.dentist.service;

import com.example.dentist.document.DynamicInfoDoc;
import com.example.dentist.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.SearchNameDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.dentist.repository.DynamicInfoRepository;
import com.example.dentist.repository.StaticInfoRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final StaticInfoRepository staticInfoRepository;


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

    public StaticInfoDoc findStaticInfoById(String id){
        return staticInfoRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));
    }

    public List<DynamicInfoDoc> openDentistsByDayTime(String day, String queryTimeStr){
        return dynamicInfoRepository.findOpenDentists(day, queryTimeStr);
    }

    public List<DynamicInfoDoc> findOpenDentistsByNow(String day, String currentTime){
        return dynamicInfoRepository.findOpenDentists(day, currentTime);
    }

    public List<DynamicInfoDoc> findByTreatCate(String category){
        return dynamicInfoRepository.findByTreatCate(category);
    }

    public List<DynamicInfoDoc> findByNameContaining(String name){
        return dynamicInfoRepository.findByNameContaining(name);
    }
}
