package com.example.domain.dentist.converter;

import annotation.Converter;
import com.example.dentist.document.DynamicInfoDoc;
import com.example.dentist.document.StaticInfoDoc;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.dentist.repository.StaticInfoRepository;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Converter
@AllArgsConstructor
public class DentistConverter {

    private final StaticInfoRepository staticInfoRepository;

    public List<DentistDto> toDentistDtos(List<DynamicInfoDoc> dynamicInfoDocs){

        List<String> matchingDentistIds = dynamicInfoDocs.stream()
                .map(DynamicInfoDoc::getId)
                .collect(Collectors.toList());

        // StaticInfoDoc 리스트 가져오기
        List<StaticInfoDoc> staticInfos = staticInfoRepository.findAllById(matchingDentistIds);

        // DentistDto 변환 및 매핑
        List<DentistDto> dentistDtos = staticInfos.stream().map(staticInfo -> {
            DynamicInfoDoc dynamicInfo = dynamicInfoDocs.stream()
                    .filter(d -> d.getId().equals(staticInfo.getId()))
                    .findFirst()
                    .orElse(null);

            return DentistDto.builder()
                    .id(staticInfo.getId())
                    .name(staticInfo.getName())
                    .addr(staticInfo.getAddr())
                    .dong(staticInfo.getDong())
                    .tele(staticInfo.getTele())
                    .img(staticInfo.getImg())
//                    .latitude(staticInfo.getLat())
//                    .longitude(staticInfo.getLon())
                    .subwayInfo(staticInfo.getSubwayInfo())
                    .subwayName(staticInfo.getSubwayName())
                    .dist(staticInfo.getDist())
                    .build();
        }).collect(Collectors.toList());
        return dentistDtos;

    }
}
