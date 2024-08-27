package com.example.domain.dentist.controller;

import api.Api;
import api.Result;
import com.example.domain.dentist.business.CategoryDentistBusiness;
import com.example.domain.dentist.business.DentistBusiness;
import com.example.domain.dentist.business.OpenDentistBusiness;
import com.example.domain.dentist.business.PersonalizedDentistBusiness;
import com.example.domain.dentist.controller.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/find")
public class PublicDentistController {

    private final PersonalizedDentistBusiness personalizedDentistBusiness;
    private final OpenDentistBusiness openDentistBusiness;
    private final CategoryDentistBusiness categoryDentistBusiness;
    private final DentistBusiness dentistBusiness;


    @PostMapping("/dentist")
    public Api<List<DentistDto>> personalizedDenDis(
            @RequestBody PersonalizedDentLocDto personalizedDentLocDto,
            @RequestParam(required = false, name="lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5",name = "limit") int limit

    ) {
        List<DentistDto> dentists = personalizedDentistBusiness.openPersonalDentist(personalizedDentLocDto, lastDentistId, limit);
        return new Api<>(new Result(200, "성공", "특정 날,시간에 문연 치과 조회 성공"), dentists);
    }

    @PostMapping("/open-dentist")
    public Api<List<DentistDto>> findNearbyDentists(
            @RequestBody LocationGuDto locationGuDtoDto,
            @RequestParam(required = false,name = "lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5",name = "limit") int limit) {
        List<DentistDto> dentists = openDentistBusiness.openDentistNow(locationGuDtoDto, lastDentistId, limit);
        return new Api<>(new Result(200, "성공", "문연 치과 조회 성공"), dentists);
    }



    @PostMapping("/cat-dentist")
    public Api<List<DentistDto>> categoryDentist(
            @RequestBody CategoryLocDto categoryLocDto,
            @RequestParam(required = false,name = "lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5",name = "limit") int limit
    ) {
        List<DentistDto> dentists = categoryDentistBusiness.categoryDentist(categoryLocDto,lastDentistId,limit);
        return new Api<>(new Result(200, "성공", "카테고리 치과 조회 성공"), dentists);
    }

    @GetMapping("/{id}")
    public Api<DentistDetail> getDentistById(
            @PathVariable("id") String id
    ) {
        DentistDetail dentist = dentistBusiness.findDentist(id);
        return new Api<>(new Result(200, "성공", "해당 치과 상세 검색 성공"), dentist);
    }

    // 병원 이름으로 검색
    @PostMapping("/by-name")
    public Api<List<DentistDto>> getDentistsByName(
            @RequestBody SearchNameDto searchNameDto,
            @RequestParam(required = false,name = "lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5",name = "limit") int limit
    ) {
        List<DentistDto> dentists = dentistBusiness.findDentistByName(searchNameDto,lastDentistId,limit);
        return new Api<>(new Result(200, "성공", "이름으로 병원 검색 성공"), dentists);
    }


}
