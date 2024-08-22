package com.example.domain.dentist.controller;

import com.example.domain.dentist.business.CategoryDentistBusiness;
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
@RequestMapping("/api/find")
public class LoginDentistController {

    private final PersonalizedDentistBusiness personalizedDentistBusiness;
    private final OpenDentistBusiness openDentistBusiness;
    private final CategoryDentistBusiness categoryDentistBusiness;


    @GetMapping("/dentist")
    public ResponseEntity<List<DentistDto>> personalizedDenDisSaved(
            @RequestParam("day") String day,
            @RequestParam("local_time") String local_time,
            @RequestHeader("Authorization") String token
    ) {
        LocalTime queryTime = LocalTime.parse(local_time);
        PersonalizedDentDto personalizedDentistDto = new PersonalizedDentDto(day, queryTime);

        List<DentistDto> dentists = personalizedDentistBusiness.personalizedDentistBySavedDis(personalizedDentistDto, token);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/open-dentist")
    public List<DentistDto> openDentistSaved(
            @RequestHeader("Authorization") String token
    ){
        return openDentistBusiness.openDentistSavedNow(token);
    }

    @GetMapping("/cat-dentist")
    public ResponseEntity<List<DentistDto>> categoryDentistSaved(
            @RequestParam("category") String category,
            @RequestHeader("Authorization") String token
    ) {
        CategoryDto categoryDto = new CategoryDto(category);
        List<DentistDto> dentists = categoryDentistBusiness.categoryDentistSaved(categoryDto, token);
        return ResponseEntity.ok(dentists);
    }
}
