package com.example.domain.dentist.service;

import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.LocationGuDto;
import com.example.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OpenDentistService {

    private final MongoTemplate mongoTemplate;

    public List<DentistInfoDoc> openDentist(LocationGuDto locationGuDto, String lastDentistId, int limit) {

        // 현재 시간과 요일
        String currentTimeStr = TimeUtil.getCurrentTime().toString();
        String day = TimeUtil.getCurrentDayOfWeek();

        log.info("{}, {}", currentTimeStr, day);


        Query query = new Query();

        query.addCriteria(Criteria.where("timeInfo." + day + ".work_time.0").lte(currentTimeStr));
        query.addCriteria(Criteria.where("timeInfo." + day + ".work_time.1").gte(currentTimeStr));
        query.addCriteria(Criteria.where("timeInfo." + day + ".description").not().regex("휴무"));
        query.addCriteria(Criteria.where("gu").regex(locationGuDto.getGu()));
        query.addCriteria(Criteria.where("location")
                .nearSphere(new Point(locationGuDto.getLongitude(), locationGuDto.getLatitude())));

        // lastDentistId가 있는 경우, 시작 인덱스 설정
        List<DentistInfoDoc> allDentists = mongoTemplate.find(query, DentistInfoDoc.class);

        int startIndex = 0;
        if (lastDentistId != null) {
            for (int i = 0; i < allDentists.size(); i++) {
                if (allDentists.get(i).getId().equals(lastDentistId)) {
                    startIndex = i + 1; // 다음 병원부터 시작
                    break;
                }
            }
        }

        // 페이지네이션된 결과 반환
        return allDentists.stream()
                .skip(startIndex)
                .limit(limit)
                .collect(Collectors.toList());
    }

}
