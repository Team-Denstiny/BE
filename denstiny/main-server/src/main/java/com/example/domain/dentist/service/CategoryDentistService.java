package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.CategoryDto;
import com.example.domain.dentist.controller.model.CategoryLocDto;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.jwt.JWTUtil;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryDentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final StaticInfoRepository staticInfoRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    public List<DentistDto> categoryDentist(
            CategoryLocDto categoryLocDto
    ){
        String category = categoryLocDto.getCategory();
        Double latitude = categoryLocDto.getLatitude();
        Double longitude = categoryLocDto.getLongitude();

        log.info("{}, {} , {}",category,latitude, longitude);

        // Null 체크
        if (category == null || latitude == null || longitude == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        List<DentistDto> dentistDtos = getDentistDtos(category);

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        return dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());
    }



    public List<DentistDto> categoryDentistSaved(
            CategoryDto categoryDto, String token
    ){
        String category = categoryDto.getCategory();

        log.info("{}",category);

        // Null 체크
        if (category == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userRepository.findByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();
        log.info("{}, {}", latitude, longitude);

        List<DentistDto> dentistDtos = getDentistDtos(category);

        // 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }

    private List<DentistDto> getDentistDtos(String category) {
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
//                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return dentistDtos;
    }
}
