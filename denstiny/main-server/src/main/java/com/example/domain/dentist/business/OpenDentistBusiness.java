package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DynamicInfoDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.LocationDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.domain.dentist.service.DentistService;
import com.example.domain.user.service.UserService;
import com.example.jwt.JWTUtil;
import com.example.user.UserEntity;
import com.example.util.DistanceUtil;
import com.example.util.TimeUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Business
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OpenDentistBusiness {

    private final DentistService dentistService;
    private final DentistConverter dentistConverter;
    private final UserService userService;
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

        String currentTimeStr = currentTime.toString();

        // 현재 요일과 시간에 열린 병원만 필터링
        List<DynamicInfoDoc> openDentists = dentistService.findOpenDentistsByNow(day, currentTimeStr);
        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(openDentists);


        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;
    }

    public List<DentistDto> openDentistSavedNow(String token){
        // 현재 시간과 요일
        LocalTime currentTime = TimeUtil.getCurrentTime();
        String day = TimeUtil.getCurrentDayOfWeek();

        log.info("{}, {}", currentTime, day);

        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userService.getUserByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();

        log.info("{}, {}", latitude, longitude);

        String currentTimeStr = currentTime.toString();
        // 현재 요일과 시간에 열린 병원만 필터링
        List<DynamicInfoDoc> openDentists = dentistService.findOpenDentistsByNow(day, currentTimeStr);

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(openDentists);

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }
}
