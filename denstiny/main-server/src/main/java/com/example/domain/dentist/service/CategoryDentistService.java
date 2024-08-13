package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.CategoryDto;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryDentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final StaticInfoRepository staticInfoRepository;

    public List<DentistDto> categoryDentist(
            CategoryDto categoryDto
    ){
        String category = categoryDto.getCategory();
        Double latitude = categoryDto.getLatitude();
        Double longitude = categoryDto.getLongitude();

        log.info("{}, {} , {}",category,latitude, longitude);

        // Null 체크
        if (category == null || latitude == null || longitude == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        // 카테고리로 동적 정보 조회
        List<DynamicInfoDoc> dynamicInfoList = dynamicInfoRepository.findByTreatCate(category);

        // 동적 정보의 ID 목록으로 정적 정보 한 번에 조회
        List<String> ids = dynamicInfoList.stream()
                .map(DynamicInfoDoc::getId)
                .collect(Collectors.toList());

        Map<String, StaticInfoDoc> staticInfoMap = staticInfoRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(StaticInfoDoc::getId, staticInfoDoc -> staticInfoDoc));

        List<DentistDto> dentistDtos = dynamicInfoList.stream()
                .map(dynamicInfo -> {
                    StaticInfoDoc staticInfo = staticInfoMap.get(dynamicInfo.getId());

                    if (staticInfo == null) return null;  // 정적 정보가 없는 경우 null 반환

                    return DentistDto.builder()
                            .id(staticInfo.getId())
                            .name(staticInfo.getName())
                            .addr(staticInfo.getAddr())
                            .dong(staticInfo.getDong())
                            .tele(staticInfo.getTele())
                            .img(staticInfo.getImg())
                            .latitude(staticInfo.getLat())
                            .longitude(staticInfo.getLon())
                            .score(dynamicInfo.getScore())
                            .reviewCnt(dynamicInfo.getReviewCnt())
                            .subwayInfo(staticInfo.getSubwayInfo())
                            .subwayName(staticInfo.getSubwayName())
                            .dist(staticInfo.getDist())
                            .build();
                })
//                .filter(Objects::nonNull)  // null 제거
                .collect(Collectors.toList());

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        return dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());
    }
}

//        List<DynamicInfoDoc> byTreatCate = dynamicInfoRepository.findByTreatCate(category);
//
//        List<DentistDto> dentistDtos = byTreatCate.stream().map(it -> {
//            StaticInfoDoc staticInfo = staticInfoRepository.findById(it.getId()).orElse(null);
//
//            return DentistDto.builder()
//                    .id(staticInfo.getId())
//                    .name(staticInfo.getName())
//                    .addr(staticInfo.getAddr())
//                    .dong(staticInfo.getDong())
//                    .tele(staticInfo.getTele())
//                    .img(staticInfo.getImg())
//                    .latitude(staticInfo.getLat())
//                    .longitude(staticInfo.getLon())
//                    .score(it.getScore())
//                    .reviewCnt(it.getReviewCnt())
//                    .subwayInfo(staticInfo.getSubwayInfo())
//                    .subwayName(staticInfo.getSubwayName())
//                    .dist(staticInfo.getDist())
//                    .build();
//        }).collect(Collectors.toList());
//
//        List<DentistDto> sortedHospitals = dentistDtos.stream()
//                .sorted(Comparator.comparingDouble(dto ->
//                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
//                .collect(Collectors.toList());
//
//        return sortedHospitals;
//    }
//}
