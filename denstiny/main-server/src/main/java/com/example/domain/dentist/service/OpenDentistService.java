package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.document.TimeDataDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.LocationDto;
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
    private final StaticInfoRepository staticInfoRepository;
    private final DynamicInfoRepository dynamicInfoRepository;
    private final UserRepository userRepository;
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

        List<DentistDto> dentistDtos = getDentistDtos(day, currentTime);

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

        List<DentistDto> dentistDtos = getDentistDtos(day, currentTime);

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }

    private List<DentistDto> getDentistDtos(String day, LocalTime currentTime) {
        // 현재 요일과 시간에 열린 병원만 필터링
        List<DynamicInfoDoc> openDentists = dynamicInfoRepository.findOpenDentists(day, currentTime.toString());

        List<String> matchingDentistIds = openDentists.stream()
                .map(DynamicInfoDoc::getId)
                .collect(Collectors.toList());

        // StaticInfoRepository를 이용해 matchingDentistIds로 StaticInfo 목록을 조회
        List<StaticInfoDoc> staticInfos = staticInfoRepository.findAllById(matchingDentistIds);

        List<DentistDto> dentistDtos = staticInfos.stream().map(staticInfo -> {
            DynamicInfoDoc dynamicInfo = openDentists.stream()
                    .filter(d -> d.getId().equals(staticInfo.getId()))
                    .findFirst()
                    .orElse(null);

            return DentistDto.builder()
                    .id(staticInfo.getId())
                    .name(staticInfo.getName())
                    .addr(staticInfo.getAddr())
                    .dong(staticInfo.getDong())
                    .tele(staticInfo.getTele())
                    .img(staticInfo.getImg())
                    .latitude(staticInfo.getLat())
                    .longitude(staticInfo.getLon())
                    .score(dynamicInfo != null ? dynamicInfo.getScore() : null)
                    .reviewCnt(dynamicInfo != null ? dynamicInfo.getReviewCnt() : null)
                    .subwayInfo(staticInfo.getSubwayInfo())
                    .subwayName(staticInfo.getSubwayName())
                    .dist(staticInfo.getDist())
                    .build();
        }).collect(Collectors.toList());
        return dentistDtos;
    }
}

