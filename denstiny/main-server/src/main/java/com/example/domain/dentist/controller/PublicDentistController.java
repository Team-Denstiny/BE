package com.example.domain.dentist.controller;

import com.example.domain.dentist.business.CategoryDentistBusiness;
import com.example.domain.dentist.business.DentistBusiness;
import com.example.domain.dentist.business.OpenDentistBusiness;
import com.example.domain.dentist.business.PersonalizedDentistBusiness;
import com.example.domain.dentist.controller.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/find")
public class PublicDentistController {

    private final PersonalizedDentistBusiness personalizedDentistBusiness;
    private final OpenDentistBusiness openDentistBusiness;
    private final CategoryDentistBusiness categoryDentistBusiness;
    private final DentistBusiness dentistBusiness;

    @GetMapping("/dentist")
    public ResponseEntity<List<DentistDto>> personalizedDenDis(
            @RequestParam("day") String day,
            @RequestParam("local_time") String local_time,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        LocalTime queryTime = LocalTime.parse(local_time);
        PersonalizedDentLocDto requestDto = new PersonalizedDentLocDto(day, queryTime, latitude, longitude);

        List<DentistDto> dentists = personalizedDentistBusiness.personalizedDentistByDis(requestDto);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/open-dentist")
    public ResponseEntity<List<DentistDto>> openDentist(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        LocationDto requestDto = new LocationDto(latitude, longitude);

        List<DentistDto> dentists = openDentistBusiness.openDentistNow(requestDto);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/cat-dentist")
    public ResponseEntity<List<DentistDto>> categoryDentist(
            @RequestParam("category") String category,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        CategoryLocDto categoryLocDto = new CategoryLocDto(category, latitude, longitude);
        List<DentistDto> dentists = categoryDentistBusiness.categoryDentist(categoryLocDto);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DentistDetail> getDentistById(
            @PathVariable("id") String id
    ) {
        DentistDetail dentist = dentistBusiness.findDentist(id);
        return ResponseEntity.ok(dentist);
    }

    // 병원 이름으로 검색
    @GetMapping("/by-name")
    public ResponseEntity<List<DentistDto>> getDentistsByName(
            @RequestParam("name") String name,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude
    ) {
        SearchNameDto searchNameDto = new SearchNameDto(name, latitude, longitude);

        List<DentistDto> dentists = dentistBusiness.findDentistByName(searchNameDto);
        return ResponseEntity.ok(dentists);
    }
}
