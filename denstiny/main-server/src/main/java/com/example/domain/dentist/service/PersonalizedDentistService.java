package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.PersonalizedDentDto;
import com.example.domain.dentist.controller.model.PersonalizedDentLocDto;
import com.example.jwt.JWTUtil;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PersonalizedDentistService {

    private final StaticInfoRepository staticInfoRepository;
    private final DynamicInfoRepository dynamicInfoRepository;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public List<DentistDto> personalizedDentistByDis(
            PersonalizedDentLocDto personalizedDentLocDto
    ) {

        // 검색할 요일, 시간, 위도, 경도
        String day = personalizedDentLocDto.getDay();
        LocalTime queryTime = personalizedDentLocDto.getLocalTime();
        Double latitude = personalizedDentLocDto.getLatitude();
        Double longitude = personalizedDentLocDto.getLongitude();

        log.info("{}, {} , {}, {}", day, queryTime, latitude, longitude);

        // Null 체크
        if (day == null || queryTime == null || latitude == null || longitude == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        String queryTimeStr = queryTime.toString();

        List<DentistDto> dentistDtos = getDentistDtos(day, queryTimeStr);

        // 거리 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;
    }



    public List<DentistDto> personalizedDentistByDisSaved(
            PersonalizedDentDto personalizedDentDto, String token
    ) {

        // 검색할 요일, 시간
        String day = personalizedDentDto.getDay();
        LocalTime queryTime = personalizedDentDto.getLocalTime();


        log.info("{}, {}", day, queryTime);

        // Null 체크
        if (day == null || queryTime == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userRepository.findByResourceId(resourceId);

        String queryTimeStr = queryTime.toString();
        List<DentistDto> dentistDtos = getDentistDtos(day, queryTimeStr);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();
        log.info("{}, {}", latitude, longitude);

        // 거리 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude,longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;
    }


    private List<DentistDto> getDentistDtos(String day, String queryTimeStr) {
        // DynamicInfoRepository의 수정된 쿼리 메서드 호출
        List<DynamicInfoDoc> openDentists = dynamicInfoRepository.findOpenDentists(day, queryTimeStr);

        List<String> matchingDentistIds = openDentists.stream()
                .map(DynamicInfoDoc::getId)
                .collect(Collectors.toList());

        // StaticInfoDoc 리스트 가져오기
        List<StaticInfoDoc> staticInfos = staticInfoRepository.findAllById(matchingDentistIds);

        // DentistDto 변환 및 매핑
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











