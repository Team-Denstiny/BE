package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.SearchNameDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final StaticInfoRepository staticInfoRepository;
    private final DentistConverter dentistConverter;

    // 병원 상세 페이지
    public DentistDetail findDentist(String id){

        log.info("id = {}",id);

        DynamicInfoDoc dynamicInfoDoc = dynamicInfoRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));

        StaticInfoDoc staticInfoDoc = staticInfoRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));

        return dentistConverter.toDentistDto(dynamicInfoDoc, staticInfoDoc);

    }

    // 병원 이름으로 검색 -> 근거리로 병원 정렬
    public List<DentistDto> findDentistByName(SearchNameDto searchNameDto){

        String name = searchNameDto.getName();
        Double latitude = searchNameDto.getLatitude();
        Double longitude = searchNameDto.getLongitude();

        log.info("search = {}", name);

        List<DynamicInfoDoc> dynamicInfoDocs = dynamicInfoRepository.findByNameContaining(name);

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(dynamicInfoDocs);

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }
}
