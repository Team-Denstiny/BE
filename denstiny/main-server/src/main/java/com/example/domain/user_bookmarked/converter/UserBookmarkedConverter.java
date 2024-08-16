package com.example.domain.user_bookmarked.converter;

import annotation.Converter;
import com.example.document.DynamicInfoDoc;
import com.example.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.repository.DynamicInfoRepository;
import com.example.repository.StaticInfoRepository;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Converter
@RequiredArgsConstructor
public class UserBookmarkedConverter {

    private final DynamicInfoRepository dynamicInfoRepository;
    private final StaticInfoRepository staticInfoRepository;

    public List<DentistDto> toDentistDtos(List<String> hospitalIds) {

        return hospitalIds.stream()
                .map(dentistId -> {

                    DynamicInfoDoc dynamicInfoDoc = dynamicInfoRepository
                            .findById(dentistId)
                            .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "없는 병원 id입니다"));

                    StaticInfoDoc staticInfoDoc = staticInfoRepository
                            .findById(dentistId)
                            .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "없는 병원 id입니다"));

                    return DentistDto.builder()
                            .id(staticInfoDoc.getId())
                            .name(staticInfoDoc.getName())
                            .addr(staticInfoDoc.getAddr())
                            .dong(staticInfoDoc.getDong())
                            .tele(staticInfoDoc.getTele())
                            .img(staticInfoDoc.getImg())
                            .score(dynamicInfoDoc.getScore())
                            .longitude(staticInfoDoc.getLon())
                            .latitude(staticInfoDoc.getLat())
                            .reviewCnt(dynamicInfoDoc.getReviewCnt())
                            .subwayInfo(staticInfoDoc.getSubwayInfo())
                            .subwayName(staticInfoDoc.getSubwayName())
                            .dist(staticInfoDoc.getDist())
                            .build();
                }).collect(Collectors.toList());
    }
}
