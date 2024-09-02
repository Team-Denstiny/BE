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
public class RedisPersonalDentistService {

    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, List<DentistInfoDoc>> redisTemplate;

    public List<DentistInfoDoc> openPersonalDentist(PersonalizedDentLocDto personalizedDentLocDto, String lastDentistId, int limit) {
        // 캐시 키 생성
        String cacheKey = createCacheKey(personalizedDentLocDto);

        // Redis에서 캐시 조회
        List<DentistInfoDoc> cachedDentists = redisTemplate.opsForValue().get(cacheKey);

        if (cachedDentists == null) {
            // 캐시가 없으면 데이터베이스에서 조회 후 캐싱
            cachedDentists = fetchAndCacheDentists(personalizedDentLocDto, cacheKey);
        }

        // 캐시된 데이터에서 lastDentistId와 limit를 기반으로 필터링
        return getDentistsByPagination(cachedDentists, lastDentistId, limit);
    }

    private String createCacheKey(PersonalizedDentLocDto personalizedDentLocDto) {
        return personalizedDentLocDto.getLongitude() + ":" +
                personalizedDentLocDto.getLatitude() + ":" +
                personalizedDentLocDto.getDay() + ":" +
                personalizedDentLocDto.getLocalTime().toString() + ":" +
                personalizedDentLocDto.getGu();
    }

    private List<DentistInfoDoc> fetchAndCacheDentists(PersonalizedDentLocDto personalizedDentLocDto, String cacheKey) {
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

        // 결과를 Redis에 캐싱 (TTL 30분 설정)
        redisTemplate.opsForValue().set(cacheKey, allDentists, Duration.ofMinutes(30));

        return allDentists;
    }

    private List<DentistInfoDoc> getDentistsByPagination(List<DentistInfoDoc> cachedDentists, String lastDentistId, int limit) {
        int startIndex = 0;

        if (lastDentistId != null) {
            for (int i = 0; i < cachedDentists.size(); i++) {
                if (cachedDentists.get(i).getId().equals(lastDentistId)) {
                    startIndex = i + 1;
                    break;
                }
            }
        }

        return cachedDentists.stream()
                .skip(startIndex)
                .limit(limit)
                .collect(Collectors.toList());
    }
}