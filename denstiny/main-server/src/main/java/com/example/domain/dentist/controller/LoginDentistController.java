package com.example.domain.dentist.controller;

import com.example.domain.dentist.controller.model.*;
import com.example.domain.dentist.service.CategoryDentistService;
import com.example.domain.dentist.service.OpenDentistService;
import com.example.domain.dentist.service.PersonalizedDentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/find")
public class LoginDentistController {

    private final PersonalizedDentistService personalizedDentistService;
    private final OpenDentistService openDentistService;
    private final CategoryDentistService categoryDentistService;


    @PostMapping("/dentist")
    public List<DentistDto> personalizedDenDisSaved(
            @RequestBody PersonalizedDentDto personalizedDentDto,
            @RequestHeader("Authorization") String token
    ){
        return personalizedDentistService.personalizedDentistByDisSaved(personalizedDentDto,token);
    }

    @GetMapping("/open-dentist")
    public List<DentistDto> openDentistSaved(
            @RequestHeader("Authorization") String token
    ){
        return openDentistService.openDentistNowSaved(token);
    }

    @PostMapping("/cat-dentist")
    public List<DentistDto> categoryDentistSaved(
            @RequestBody CategoryDto categoryDto,
            @RequestHeader("Authorization") String token
    ){
        return categoryDentistService.categoryDentistSaved(categoryDto,token);
    }
}
