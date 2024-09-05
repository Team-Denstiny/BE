package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.CategoryDto;
import com.example.domain.dentist.controller.model.CategoryLocDto;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.converter.DentistInfoConverter;
import com.example.domain.dentist.service.CategoryDentistService;
import com.example.domain.user.service.UserService;
import com.example.jwt.JWTUtil;
import com.example.user.UserEntity;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryDentistBusiness {

    private final CategoryDentistService categoryDentistService;
    private final DentistInfoConverter dentistInfoConverter;

    private final JWTUtil jwtUtil;
    private final UserService UserService;

    public List<DentistDto> categoryDentist(
            CategoryLocDto categoryLocDto,
            String lastDentistId,
            int limit
    ){
        String category = categoryLocDto.getCategory();
        String gu = categoryLocDto.getGu();
        Double latitude = categoryLocDto.getLatitude();
        Double longitude = categoryLocDto.getLongitude();

        log.info("{}, {} ,{} ,{}", category, latitude, longitude, gu);

        // Null 체크
        if (category == null || latitude == null || longitude == null || gu == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }

        List<DentistInfoDoc> dentistInfoDocs = categoryDentistService.categoryDentist(categoryLocDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(dentistInfoDocs);

        return dentistDtos;

    }

    public List<DentistDto> categoryDentistSaved(
            CategoryDto categoryDto,
            String lastDentistId,
            int limit,
            String token
    ){
        String category = categoryDto.getCategory();
        String gu = categoryDto.getGu();


        log.info("{}, {}", category, gu);

        String access = token.split(" ")[1];
        String resourceId = jwtUtil.getResourceId(access);
        UserEntity user = UserService.getUserByResourceId(resourceId);

        Double latitude = user.getLatitude();
        Double longitude = user.getLongitude();

        log.info("{}, {}", latitude, longitude);

        // Null 체크
        if (category == null || latitude == null || longitude == null || gu == null) {
            throw new ApiException(ErrorCode.NULL_POINT, "DTO 필드 값이 null입니다.");
        }


        CategoryLocDto categoryLocDto = CategoryLocDto.builder()
                .category(category)
                .gu(gu)
                .longitude(longitude)
                .latitude(latitude)
                .build();

        List<DentistInfoDoc> dentistInfoDocs = categoryDentistService.categoryDentist(categoryLocDto, lastDentistId, limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(dentistInfoDocs);

        return dentistDtos;

    }
}
