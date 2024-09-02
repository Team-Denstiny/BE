package com.example.domain.dentist.service;

import com.example.dentist.document.DentistInfoDoc;
import com.example.domain.dentist.controller.model.PersonalizedDentLocDto;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class RedisPersonalDentistService2 {

    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, String> redisTemplate; // 객체 ID를 저장할 RedisTemplate

    public List<DentistInfoDoc> openPersonalDentist(PersonalizedDentLocDto personalizedDentLocDto, String lastDentistId, int limit) {

        long startTime = System.currentTimeMillis();

        // 캐시 키 생성
        String cacheKey = createCacheKey(personalizedDentLocDto);

        // Redis에서 캐시된 ID 리스트 조회
        List<String> cachedDentistIds = redisTemplate.opsForList().range(cacheKey, 0, -1);

        long middleTime = System.currentTimeMillis();
        System.out.println("Middle time: " + (middleTime - startTime) + "ms");

        if (cachedDentistIds == null || cachedDentistIds.isEmpty()) {
            // 캐시가 없으면 데이터베이스에서 조회 후 캐싱
            cachedDentistIds = fetchAndCacheDentistIds(personalizedDentLocDto, cacheKey);
        }

        // ID 리스트로 MongoDB에서 객체 가져오기 및 페이징 처리
        List<DentistInfoDoc> result = getDentistsByPagination(cachedDentistIds, lastDentistId, limit);

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime) + "ms");

        return result;
    }

    private String createCacheKey(PersonalizedDentLocDto personalizedDentLocDto) {
        return personalizedDentLocDto.getLongitude() + ":" +
                personalizedDentLocDto.getLatitude() + ":" +
                personalizedDentLocDto.getDay() + ":" +
                personalizedDentLocDto.getLocalTime().toString() + ":" +
                personalizedDentLocDto.getGu();
    }

    private List<String> fetchAndCacheDentistIds(PersonalizedDentLocDto personalizedDentLocDto, String cacheKey) {

        long startTime = System.currentTimeMillis();

        Double longitude = personalizedDentLocDto.getLongitude();
        Double latitude = personalizedDentLocDto.getLatitude();
        String day = personalizedDentLocDto.getDay();
        String currentTimeStr = personalizedDentLocDto.getLocalTime().toString();
        String gu = personalizedDentLocDto.getGu();

        Query query = new Query();
        query.addCriteria(Criteria.where("gu").is(gu));
        query.addCriteria(Criteria.where("timeInfo." + day + ".work_time.0").lte(currentTimeStr));
        query.addCriteria(Criteria.where("timeInfo." + day + ".work_time.1").gte(currentTimeStr));
        query.addCriteria(Criteria.where("timeInfo." + day + ".description").not().regex("휴무"));
        query.addCriteria(Criteria.where("location").nearSphere(new Point(longitude, latitude)));
        List<DentistInfoDoc> allDentists = mongoTemplate.find(query, DentistInfoDoc.class);

        long queryEndTime = System.currentTimeMillis();
        System.out.println("MongoDB query execution time: " + (queryEndTime - startTime) + "ms");


        // 결과 ID를 Redis에 캐싱 (TTL 30분 설정)
        List<String> dentistIds = allDentists.stream()
                .map(DentistInfoDoc::getId)
                .collect(Collectors.toList());

        redisTemplate.opsForList().rightPushAll(cacheKey, dentistIds);
        redisTemplate.expire(cacheKey, Duration.ofMinutes(30));

        long processingEndTime = System.currentTimeMillis();
        System.out.println("Data processing time: " + (processingEndTime - queryEndTime) + "ms");


        return dentistIds;
    }

    private List<DentistInfoDoc> getDentistsByPagination(List<String> dentistIds, String lastDentistId, int limit) {
        int startIndex = 0;

        if (lastDentistId != null) {
            for (int i = 0; i < dentistIds.size(); i++) {
                if (dentistIds.get(i).equals(lastDentistId)) {
                    startIndex = i + 1;
                    break;
                }
            }
        }

        List<String> paginatedIds = dentistIds.stream()
                .skip(startIndex)
                .limit(limit)
                .collect(Collectors.toList());

        // ID 리스트로 MongoDB에서 객체 가져오기
        Query query = new Query(Criteria.where("_id").in(paginatedIds));
        return mongoTemplate.find(query, DentistInfoDoc.class);
    }
}
