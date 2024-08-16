package com.example.domain.dentist.controller;

import com.example.domain.dentist.controller.model.*;
import com.example.domain.dentist.service.CategoryDentistService;
import com.example.domain.dentist.service.OpenDentistService;
import com.example.domain.dentist.service.PersonalizedDentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/find")
public class LoginDentistController {

    private final PersonalizedDentistService personalizedDentistService;
    private final OpenDentistService openDentistService;
    private final CategoryDentistService categoryDentistService;


    @GetMapping("/dentist")
    public ResponseEntity<List<DentistDto>> personalizedDenDisSaved(
            @RequestParam("day") String day,
            @RequestParam("local_time") String local_time,
            @RequestHeader("Authorization") String token
    ) {
        LocalTime queryTime = LocalTime.parse(local_time);
        PersonalizedDentDto personalizedDentistDto = new PersonalizedDentDto(day, queryTime);

        List<DentistDto> dentists = personalizedDentistService.personalizedDentistByDisSaved(personalizedDentistDto, token);
        return ResponseEntity.ok(dentists);
    }

    @GetMapping("/open-dentist")
    public List<DentistDto> openDentistSaved(
            @RequestHeader("Authorization") String token
    ){
        return openDentistService.openDentistNowSaved(token);
    }

    @GetMapping("/cat-dentist")
    public ResponseEntity<List<DentistDto>> categoryDentistSaved(
            @RequestParam("category") String category,
            @RequestHeader("Authorization") String token
    ) {
        CategoryDto categoryDto = new CategoryDto(category);
        List<DentistDto> dentists = categoryDentistService.categoryDentistSaved(categoryDto, token);
        return ResponseEntity.ok(dentists);
    }
}
