package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.document.TimeDataDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.LocationDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.jwt.JWTUtil;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.util.DistanceUtil;
import com.example.util.TimeUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenDentistService {
    private final DynamicInfoRepository dynamicInfoRepository;
    private final UserRepository userRepository;
    private final DentistConverter dentistConverter;
    private final JWTUtil jwtUtil;

    public List<DentistDto> openDentistNow(
            LocationDto locationDto
    ) {

        // 위도, 경도
        Double latitude = locationDto.getLatitude();
        Double longitude = locationDto.getLongitude();

        log.info("{}, {}", latitude, longitude);

        // Null 체크
        if (latitude == null || longitude == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        // 현재 시간과 요일
        LocalTime currentTime = TimeUtil.getCurrentTime();
        String day = TimeUtil.getCurrentDayOfWeek();

        log.info("{}, {}", currentTime, day);

        // 현재 요일과 시간에 열린 병원만 필터링
        List<DynamicInfoDoc> openDentists = dynamicInfoRepository.findOpenDentists(day, currentTime.toString());

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(openDentists);


        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;
    }



    public List<DentistDto> openDentistNowSaved(String token){
        // 현재 시간과 요일
        LocalTime currentTime = TimeUtil.getCurrentTime();
        String day = TimeUtil.getCurrentDayOfWeek();

        log.info("{}, {}", currentTime, day);

        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userRepository.findByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();
        log.info("{}, {}", latitude, longitude);

        // 현재 요일과 시간에 열린 병원만 필터링
        List<DynamicInfoDoc> openDentists = dynamicInfoRepository.findOpenDentists(day, currentTime.toString());

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(openDentists);

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }
}

