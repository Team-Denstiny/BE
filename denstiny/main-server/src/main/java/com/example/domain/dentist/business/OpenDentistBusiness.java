package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.LocationGuDto;
import com.example.domain.dentist.converter.DentistInfoConverter;
import com.example.domain.dentist.service.OpenDentistService;
import com.example.domain.user.service.UserService;
import com.example.jwt.JWTUtil;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OpenDentistBusiness {

    private final OpenDentistService openDentistService;
    private final DentistInfoConverter dentistInfoConverter;

    private final UserService userService;
    private final JWTUtil jwtUtil;


public List<DentistDto> openDentistNow(
        LocationGuDto locationGuDto,
        String lastDentistId,
        int limit
) {

    // 위도, 경도, 구
    Double latitude = locationGuDto.getLatitude();
    Double longitude = locationGuDto.getLongitude();
    String gu = locationGuDto.getGu();

    log.info("{}, {}, {}", latitude, longitude,gu);

    // Null 체크
    if (latitude == null || longitude == null || gu == null) {
        throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
    }

    List<DentistInfoDoc> nearbyDentists = openDentistService.openDentist(locationGuDto, lastDentistId, limit);
    List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(nearbyDentists);


    return dentistDtos;
    }

    public List<DentistDto> openDentistSaved(
            String token,
            String lastDentistId,
            int limit
    ) {

        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = userService.getUserByResourceId(resourceId);

        String gu = user.getAddress().split(" ")[1];
        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();

        LocationGuDto locationGuDto = LocationGuDto.builder()
                .gu(gu)
                .longitude(longitude)
                .latitude(latitude)
                .build();

        List<DentistInfoDoc> nearbyDentists = openDentistService.openDentist(locationGuDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(nearbyDentists);


        return dentistDtos;
    }

}
