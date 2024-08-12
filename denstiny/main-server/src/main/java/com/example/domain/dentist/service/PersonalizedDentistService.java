package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.document.TimeDataDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.PersonalizedDentistDTO;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PersonalizedDentistService {

    private final StaticInfoRepository staticInfoRepository;
    private final DynamicInfoRepository dynamicInfoRepository;

    public List<DentistDto> personalizedDentistByDis(
            @RequestBody PersonalizedDentistDTO personalizedDentistDTO
    ) {

        // 검색할 요일, 시간, 위도, 경도
        String day = personalizedDentistDTO.getDay();
        LocalTime queryTime = personalizedDentistDTO.getLocalTime();
        Double latitude = personalizedDentistDTO.getLatitude();
        Double longitude = personalizedDentistDTO.getLongitude();

        log.info("{}, {} , {}, {}", day, queryTime, latitude, longitude);

        // Null 체크
        if (day == null || queryTime == null || latitude == null || longitude == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        List<DynamicInfoDoc> allDentists = dynamicInfoRepository.findAll();
        List<String> matchingDentistIds = new ArrayList<>();


        for (DynamicInfoDoc dentist : allDentists) {

            // 해당 요일 TimeData
            Map<String, TimeDataDoc> timeDataMap = dentist.getTimeDataMap();
            TimeDataDoc timeData = timeDataMap.get(day);

            // 만약 TimeData가 없는 병원이라면 continue
            if (timeData == null) continue;

            // description에 "정기휴무"가 포함된 경우 continue
            if (timeData.getDescription() != null && timeData.getDescription().contains("정기휴무")) {
                continue;
            }

            // 병원 운영시간
            List<String> workTimes = timeData.getWork_time();
//            if (workTimes == null || workTimes.size() < 2) continue;

            String startTimeString = workTimes.get(0);
            String endTimeString = workTimes.get(1);

            if ("00:00".equals(startTimeString) && "00:00".equals(endTimeString)) continue;

            // String -> LocalTime
            LocalTime startTime = LocalTime.parse(startTimeString);
            LocalTime endTime = LocalTime.parse(endTimeString);

            // 운영 시간에 쿼리시간이 포함되면 리스트에 추가
            if (queryTime.isAfter(startTime) && queryTime.isBefore(endTime)) {
                matchingDentistIds.add(dentist.getId());
            }
        }

        // StaticInfoRepository를 이용해 matchingDentistIds로 StaticInfo 목록을 조회
        List<StaticInfoDoc> staticInfos = staticInfoRepository.findAllById(matchingDentistIds);

        List<DentistDto> dentistDtos = staticInfos.stream().map(staticInfo -> {
            // 해당 StaticInfo에 맞는 DynamicInfoData를 찾음
            DynamicInfoDoc dynamicInfo = allDentists.stream()
                    .filter(d -> d.getId().equals(staticInfo.getId()))
                    .findFirst()
                    .orElse(null);

            // DentistDto 객체 생성
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

        // 현재 위치와 병원 간의 [거리를 기준]으로 병원을 정렬  -> 기본값을 거리순으로

        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;
    }
}


//    // 검색할 요일, 시간, 위도, 경도
//    String day = personalizedDentistDTO.getDay();
//    LocalTime queryTime = personalizedDentistDTO.getLocalTime();
//    Double latitude = personalizedDentistDTO.getLatitude();
//    Double longitude = personalizedDentistDTO.getLongitude();
//
//        log.info("{}, {} , {}, {}",day,queryTime,latitude,longitude);
//
//    // Null 체크
//        if (day == null || queryTime == null || latitude == null || longitude == null) {
//        throw new IllegalArgumentException("DTO 필드 값이 null입니다.");
//    }
//
//    List<DynamicInfoData> allDentists = dynamicInfoRepository.findAll();
//    List<String> matchingDentistIds = new ArrayList<>();
//
//
//        for (DynamicInfoData dentist : allDentists) {
//
//        // 해당 요일 TimeData
//        Map<String, TimeData> timeDataMap = dentist.getTimeDataMap();
//        TimeData timeData = timeDataMap.get(day);
//
//        // 만약 TimeData가 없는 병원이라면 continue
//        if (timeData == null) continue;
//
//        // description에 "정기휴무"가 포함된 경우 continue
//        if (timeData.getDescription() != null && timeData.getDescription().contains("정기휴무")) {
//            continue;
//        }
//
//        // 병원 운영시간
//        List<String> workTimes = timeData.getWork_time();
////            if (workTimes == null || workTimes.size() < 2) continue;
//
//        String startTimeString = workTimes.get(0);
//        String endTimeString = workTimes.get(1);
//
//        if ("00:00".equals(startTimeString) && "00:00".equals(endTimeString)) continue;
//
//        // String -> LocalTime
//        LocalTime startTime = LocalTime.parse(startTimeString);
//        LocalTime endTime = LocalTime.parse(endTimeString);
//
//        // 운영 시간에 쿼리시간이 포함되면 리스트에 추가
//        if (queryTime.isAfter(startTime) && queryTime.isBefore(endTime)) {
//            matchingDentistIds.add(dentist.getId());
//        }
//    }
//
//    // StaticInfoRepository를 이용해 matchingDentistIds로 StaticInfo 목록을 조회
//    List<StaticInfo> hospitals = staticInfoRepository.findAllById(matchingDentistIds);
//
//    // 현재 위치와 병원 간의 거리를 기준으로 병원을 정렬
//        return hospitals.stream()
//                .sorted(Comparator.comparingDouble(h ->
//            DistanceUtil.calculateDistance(latitude, longitude, h.getLat(), h.getLon())))
//            .collect(Collectors.toList());
//}











