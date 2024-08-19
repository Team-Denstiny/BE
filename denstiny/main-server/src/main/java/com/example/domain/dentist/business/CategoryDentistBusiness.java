package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DynamicInfoDoc;
import com.example.domain.dentist.controller.model.CategoryDto;
import com.example.domain.dentist.controller.model.CategoryLocDto;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.domain.dentist.service.DentistService;
import com.example.domain.user.service.UserService;
import com.example.jwt.JWTUtil;
import com.example.user.UserEntity;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Business
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryDentistBusiness {

    private final DentistService dentistService;
    private final JWTUtil jwtUtil;
    private final DentistConverter dentistConverter;
    private final UserService UserService;

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
        List<DynamicInfoDoc> dynamicInfoList = dentistService.findByTreatCate(category);

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
        UserEntity user = UserService.getUserByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();
        log.info("{}, {}", latitude, longitude);

        // 카테고리로 dynamicInfo 정보 조회
        List<DynamicInfoDoc> dynamicInfoList = dentistService.findByTreatCate(category);

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(dynamicInfoList);


        // 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;
    }

}
