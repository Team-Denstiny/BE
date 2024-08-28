package com.example.domain.dentist.converter;

import annotation.Converter;
import com.example.dentist.document.DentistInfoDoc;
import com.example.dentist.document.DynamicInfoDoc;
import com.example.dentist.document.StaticInfoDoc;
import com.example.dentist.document.TimeData;
import com.example.domain.dentist.controller.model.DentistDetail;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.util.TimeUtil;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Converter
@AllArgsConstructor
public class DentistInfoConverter {

    public List<DentistDto> toDentistDtos(List<DentistInfoDoc> dentistInfoDocs) {

        String currentDayOfWeek = TimeUtil.getCurrentDayOfWeek();

        return dentistInfoDocs.stream().map(dentistInfoDoc -> {
            // 현재 요일에 대한 진료 시간 정보 가져오기
            TimeData timeDataForToday = dentistInfoDoc.getTimeInfo().get(currentDayOfWeek);
            List<String> todayWork = null; // 기본값은 null

            if (timeDataForToday != null) {
                // work_time 가져오기
                List<String> workTime = timeDataForToday.getWork_time();
                if (workTime != null && workTime.size() == 2) {
                    todayWork = new ArrayList<>();
                    todayWork.add(workTime.get(0)); // 시작 시간
                    todayWork.add(workTime.get(1)); // 끝 시간
                }
            }

            return DentistDto.builder()
                    .id(dentistInfoDoc.getId())
                    .name(dentistInfoDoc.getName())
                    .addr(dentistInfoDoc.getAddress())
                    .dong(dentistInfoDoc.getDong())
                    .gu(dentistInfoDoc.getGu())
                    .tele(dentistInfoDoc.getTelephone())
                    .img(dentistInfoDoc.getImageUrl())
                    .subwayInfo(dentistInfoDoc.getSubwayInfo())
                    .subwayName(dentistInfoDoc.getSubwayName())
                    .dist(dentistInfoDoc.getDistance())
                    .todayWork(todayWork) // 현재 요일의 진료 시간 또는 null
                    .build();
        }).collect(Collectors.toList());

    }


    public DentistDetail toDentistDto(DentistInfoDoc dentistInfoDoc){
        return DentistDetail.builder()
                .name(dentistInfoDoc.getName())
                .addr(dentistInfoDoc.getAddress())
                .dong(dentistInfoDoc.getDong())
                .gu(dentistInfoDoc.getGu())
                .tele(dentistInfoDoc.getTelephone())
                .img(dentistInfoDoc.getImageUrl())
                .timeDataMap(dentistInfoDoc.getTimeInfo())
                .category(dentistInfoDoc.getTreatmentCategories())
                .subwayInfo(dentistInfoDoc.getSubwayInfo())
                .subwayName(dentistInfoDoc.getSubwayName())
                .dist(dentistInfoDoc.getDistance())
                .build();
    }
}