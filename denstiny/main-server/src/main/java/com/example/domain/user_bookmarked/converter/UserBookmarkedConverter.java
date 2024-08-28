package com.example.domain.user_bookmarked.converter;

import annotation.Converter;
import com.example.dentist.document.DentistInfoDoc;
import com.example.dentist.document.TimeData;
import com.example.dentist.repository.DentistInfoRepository;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.dentist.repository.DynamicInfoRepository;
import com.example.dentist.repository.StaticInfoRepository;
import com.example.util.TimeUtil;
import error.ErrorCode;
import exception.ApiException;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Converter
@RequiredArgsConstructor
public class UserBookmarkedConverter {

    private final DentistInfoRepository dentistInfoRepository;

    public List<DentistDto> toDentistDtos(List<String> hospitalIds) {

        return hospitalIds.stream()
                .map(dentistId -> {

                    DentistInfoDoc dentistInfo = dentistInfoRepository
                            .findById(dentistId)
                            .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "없는 병원 id입니다"));

                    String currentDayOfWeek = TimeUtil.getCurrentDayOfWeek();
                    TimeData timeDataForToday = dentistInfo.getTimeInfo().get(currentDayOfWeek);

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
                            .id(dentistInfo.getId())
                            .name(dentistInfo.getName())
                            .addr(dentistInfo.getAddress())
                            .dong(dentistInfo.getDong())
                            .gu(dentistInfo.getGu())
                            .tele(dentistInfo.getTelephone())
                            .img(dentistInfo.getImageUrl())
                            .subwayInfo(dentistInfo.getSubwayInfo())
                            .subwayName(dentistInfo.getSubwayName())
                            .dist(dentistInfo.getDistance())
                            .todayWork(todayWork)
                            .build();
                }).collect(Collectors.toList());
    }
}
