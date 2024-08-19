package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DynamicInfoDoc;
import com.example.dentist.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.SearchNameDto;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.domain.dentist.service.DentistService;
import com.example.util.DistanceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Business
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class DentistBusiness {

    private final DentistService dentistService;
    private final DentistConverter dentistConverter;

    // 병원 상세 페이지
    public DentistDetail findDentist(String id){

        log.info("id = {}", id);

        DynamicInfoDoc dynamicInfoDoc = dentistService.findDynamicInfoById(id);
        StaticInfoDoc staticInfoDoc = dentistService.findStaticInfoById(id);

        return dentistConverter.toDentistDto(dynamicInfoDoc, staticInfoDoc);
    }

    // 병원 이름으로 검색 -> 근거리로 병원 정렬
    public List<DentistDto> findDentistByName(SearchNameDto searchNameDto){

        String name = searchNameDto.getName();
        Double latitude = searchNameDto.getLatitude();
        Double longitude = searchNameDto.getLongitude();

        log.info("search = {}", name);

        List<DynamicInfoDoc> dynamicInfoDocs = dentistService.findByNameContaining(name);

        List<DentistDto> dentistDtos = dentistConverter.toDentistDtos(dynamicInfoDocs);

        // 현재 위치와 병원 간의 거리를 기준으로 병원 정렬
        List<DentistDto> sortedHospitals = dentistDtos.stream()
                .sorted(Comparator.comparingDouble(dto ->
                        DistanceUtil.calculateDistance(latitude, longitude, dto.getLatitude(), dto.getLongitude())))
                .collect(Collectors.toList());

        return sortedHospitals;

    }
}
