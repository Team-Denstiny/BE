package com.example.domain.dentist.controller;

import api.Api;
import api.Result;
import com.example.domain.dentist.business.CategoryDentistBusiness;
import com.example.domain.dentist.business.OpenDentistBusiness;
import com.example.domain.dentist.business.PersonalizedDentistBusiness;
import com.example.domain.dentist.controller.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/find")
public class LoginDentistController {

    private final PersonalizedDentistBusiness personalizedDentistBusiness;
    private final OpenDentistBusiness openDentistBusiness;
    private final CategoryDentistBusiness categoryDentistBusiness;


    @GetMapping("/open-dentist")
    public Api<List<DentistDto>> findNearbyDentists(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false, name = "lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5", name = "limit") int limit) {

        List<DentistDto> dentists = openDentistBusiness.openDentistSaved(token, lastDentistId, limit);
        return new Api<>(new Result(200, "성공", "문연 치과 조회 성공"), dentists);
    }

    @PostMapping("/dentist")
    public Api<List<DentistDto>> personalizedDenDisSaved(
            @RequestBody PersonalizedDentDto personalizedDentDto,
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false,name = "lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5",name = "limit") int limit
    ) {

        List<DentistDto> dentists = personalizedDentistBusiness.openPersonalDentistSaved(personalizedDentDto,lastDentistId,limit, token);
        return new Api<>(new Result(200, "성공", "특정 날,시간에 문연 치과 조회 성공"), dentists);
    }

    @PostMapping("/cat-dentist")
    public Api<List<DentistDto>> categoryDentistSaved(
            @RequestBody CategoryDto categoryDto,
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false,name = "lastDentistId") String lastDentistId,
            @RequestParam(defaultValue = "5",name = "limit") int limit
    ) {
        List<DentistDto> dentists = categoryDentistBusiness.categoryDentistSaved(categoryDto,lastDentistId,limit,token);
        return new Api<>(new Result(200, "성공", "카테고리 치과 조회 성공"), dentists);
    }
}
