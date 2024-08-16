package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.domain.dentist.controller.model.CategoryDto;
import com.example.domain.dentist.controller.model.CategoryLocDto;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.jwt.JWTUtil;
import com.example.repository.DynamicInfoRepository;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
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
@Transactional
@RequiredArgsConstructor
public class CategoryDentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final JWTUtil jwtUtil;
    private final DentistConverter dentistConverter;
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

        // 카테고리로 dynamicInfo 정보 조회
        List<DynamicInfoDoc> dynamicInfoList = dynamicInfoRepository.findByTreatCate(category);

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(dynamicInfoList);

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

        // 카테고리로 dynamicInfo 정보 조회
        List<DynamicInfoDoc> dynamicInfoList = dynamicInfoRepository.findByTreatCate(category);

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(dynamicInfoList);


        // 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }
}
