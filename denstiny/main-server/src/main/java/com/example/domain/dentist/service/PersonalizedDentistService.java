package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.PersonalizedDentDto;
import com.example.domain.dentist.controller.model.PersonalizedDentLocDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.jwt.JWTUtil;
import com.example.repository.DynamicInfoRepository;
import com.example.user.UserEntity;
import com.example.user.UserRepository;
import com.example.util.DistanceUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class PersonalizedDentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final DentistConverter dentistConverter;
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

        List<DynamicInfoDoc> openDentists = dynamicInfoRepository.findOpenDentists(day, queryTimeStr);
        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(openDentists);


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

        List<DynamicInfoDoc> openDentists = dynamicInfoRepository.findOpenDentists(day, queryTimeStr);
        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(openDentists);


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
}











