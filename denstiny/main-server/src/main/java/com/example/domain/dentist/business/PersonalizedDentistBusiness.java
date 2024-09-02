package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.PersonalizedDentDto;
import com.example.domain.dentist.controller.model.PersonalizedDentLocDto;
import com.example.domain.dentist.converter.DentistInfoConverter;
import com.example.domain.dentist.service.PersonalizedDentistService;
import com.example.domain.dentist.service.RedisPersonalDentistService;
import com.example.domain.dentist.service.RedisPersonalDentistService2;
import com.example.domain.user.service.UserService;
import com.example.jwt.JWTUtil;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
@Slf4j
@Business
@AllArgsConstructor
@Transactional(readOnly = true)
public class PersonalizedDentistBusiness {

    private final PersonalizedDentistService personalizedDentistService;
    private final DentistInfoConverter dentistInfoConverter;

    private final RedisPersonalDentistService redisPersonalDentistService;
    private final RedisPersonalDentistService2 redisPersonalDentistService2;
    private final UserService userService;
    private final JWTUtil jwtUtil;

    public List<DentistDto> openPersonalDentist(
            PersonalizedDentLocDto personalizedDentLocDto,
            String lastDentistId,
            int limit
    ) {

        // dto 검증!
        Double latitude = personalizedDentLocDto.getLatitude();
        Double longitude = personalizedDentLocDto.getLongitude();
        String gu = personalizedDentLocDto.getGu();
        String day = personalizedDentLocDto.getDay();
        String currentTimeStr = personalizedDentLocDto.getLocalTime().toString();

        log.info("{}, {}, {}, {}, {}", latitude, longitude, gu, day, currentTimeStr);

//        // Null 체크
//        if (latitude == null || longitude == null || gu == null || day ==null || currentTimeStr==null) {
//            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
//        }
        List<DentistInfoDoc> dentistInfoDocs = personalizedDentistService.openDentist(personalizedDentLocDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(dentistInfoDocs);

        return dentistDtos;
    }

    public List<DentistDto> openPersonalDentistRedis(
            PersonalizedDentLocDto personalizedDentLocDto,
            String lastDentistId,
            int limit
    ) {

        // dto 검증!
        Double latitude = personalizedDentLocDto.getLatitude();
        Double longitude = personalizedDentLocDto.getLongitude();
        String gu = personalizedDentLocDto.getGu();
        String day = personalizedDentLocDto.getDay();
        String currentTimeStr = personalizedDentLocDto.getLocalTime().toString();

        log.info("{}, {}, {}, {}, {}", latitude, longitude, gu, day, currentTimeStr);

        // Null 체크
        if (latitude == null || longitude == null || gu == null || day ==null || currentTimeStr==null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }
//        List<DentistInfoDoc> dentistInfoDocs = redisPersonalDentistService.openPersonalDentist(personalizedDentLocDto, lastDentistId, limit);
        List<DentistInfoDoc> dentistInfoDocs = redisPersonalDentistService2.openPersonalDentist(personalizedDentLocDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(dentistInfoDocs);

        return dentistDtos;
    }

    public List<DentistDto> openPersonalDentistSaved(
            PersonalizedDentDto personalizedDentDto,
            String lastDentistId,
            int limit,
            String token
    ) {

        // 토큰을 통해 유저의 위도, 경도 획득
        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userService.getUserByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();

        // dto 검증!
        String gu = personalizedDentDto.getGu();
        String day = personalizedDentDto.getDay();
        LocalTime currentTime = personalizedDentDto.getLocalTime();
        String currentTimeStr = currentTime.toString();

        log.info("{}, {}, {}", gu, day, currentTimeStr);

        // Null 체크
        if (gu == null || day ==null || currentTimeStr==null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        PersonalizedDentLocDto personalizedDentLocDto = PersonalizedDentLocDto.builder()
                .day(day)
                .localTime(currentTime)
                .gu(gu)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        List<DentistInfoDoc> dentistInfoDocs = personalizedDentistService.openDentist(personalizedDentLocDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(dentistInfoDocs);

        return dentistDtos;
    }

    public List<DentistDto> openPersonalDentistSavedRedis(
            PersonalizedDentDto personalizedDentDto,
            String lastDentistId,
            int limit,
            String token
    ) {

        // 토큰을 통해 유저의 위도, 경도 획득
        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userService.getUserByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();

        // dto 검증!
        String gu = personalizedDentDto.getGu();
        String day = personalizedDentDto.getDay();
        LocalTime currentTime = personalizedDentDto.getLocalTime();
        String currentTimeStr = currentTime.toString();

        log.info("{}, {}, {}", gu, day, currentTimeStr);

        // Null 체크
        if (gu == null || day ==null || currentTimeStr==null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        PersonalizedDentLocDto personalizedDentLocDto = PersonalizedDentLocDto.builder()
                .day(day)
                .localTime(currentTime)
                .gu(gu)
                .latitude(latitude)
                .longitude(longitude)
                .build();
        List<DentistInfoDoc> dentistInfoDocs = redisPersonalDentistService.openPersonalDentist(personalizedDentLocDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(dentistInfoDocs);

        return dentistDtos;
    }
}
