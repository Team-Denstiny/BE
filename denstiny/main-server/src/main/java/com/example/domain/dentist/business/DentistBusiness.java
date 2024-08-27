package com.example.domain.dentist.business;

import annotation.Business;
import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.SearchNameDto;
import com.example.domain.dentist.converter.DentistInfoConverter;
import com.example.domain.dentist.service.DentistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class DentistBusiness {

    private final DentistService dentistService;
    private final DentistInfoConverter dentistInfoConverter;
    // 병원 상세 페이지
    public DentistDetail findDentist(String id){

        log.info("id = {}", id);

        DentistInfoDoc dentistInfoDoc = dentistService.findDentistInfoById(id);
        DentistDetail dentistDto = dentistInfoConverter.toDentistDto(dentistInfoDoc);

        return dentistDto;
    }

    // 병원 이름으로 검색 -> 근거리로 병원 정렬
    public List<DentistDto> findDentistByName(SearchNameDto searchNameDto,String lastDentistId, int limit){

        String name = searchNameDto.getName();
        Double latitude = searchNameDto.getLatitude();
        Double longitude = searchNameDto.getLongitude();

        log.info("search = {}", name);

        List<DentistInfoDoc> byNameContaining = dentistService.findByNameContaining(searchNameDto,lastDentistId,limit);
        List<DentistDto> dentistDtos = dentistInfoConverter.toDentistDtos(byNameContaining);

        return dentistDtos;

    }
}
