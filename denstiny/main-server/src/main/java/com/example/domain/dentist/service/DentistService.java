package com.example.domain.dentist.service;

import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.converter.DentistConverter;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DentistService {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final StaticInfoRepository staticInfoRepository;
    private final DentistConverter dentistConverter;

    public DentistDetail findDentist(String id){

        DynamicInfoDoc dynamicInfoDoc = dynamicInfoRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));

        StaticInfoDoc staticInfoDoc = staticInfoRepository
                .findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "id로 병원을 찾을 수 없습니다"));

        return dentistConverter.toDentistDto(dynamicInfoDoc, staticInfoDoc);

    }
}
