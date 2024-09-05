package com.example.domain.dentist.service;

import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.PersonalizedDentLocDto;
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
public class PersonalizedDentistService {

    private final MongoTemplate mongoTemplate;

    public List<DentistInfoDoc> openDentist(PersonalizedDentLocDto personalizedDentLocDto, String lastDentistId, int limit) {

        Double longitude = personalizedDentLocDto.getLongitude();
        Double latitude = personalizedDentLocDto.getLatitude();

        String day = personalizedDentLocDto.getDay();
        String currentTimeStr = personalizedDentLocDto.getLocalTime().toString();
        String gu = personalizedDentLocDto.getGu();

        Query query = new Query();

        query.addCriteria(Criteria.where("timeInfo." + day + ".work_time.0").lte(currentTimeStr));
        query.addCriteria(Criteria.where("timeInfo." + day + ".work_time.1").gte(currentTimeStr));
        query.addCriteria(Criteria.where("timeInfo." + day + ".description").not().regex("휴무"));
        query.addCriteria(Criteria.where("gu").regex(gu));
        query.addCriteria(Criteria.where("location")
                .nearSphere(new Point(longitude,latitude)));

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
