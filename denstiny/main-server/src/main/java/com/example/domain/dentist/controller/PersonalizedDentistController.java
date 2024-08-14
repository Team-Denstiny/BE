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
public class PersonalizedDentistController {

    private final PersonalizedDentistService personalizedDentistService;
    private final OpenDentistService openDentistService;
    private final CategoryDentistService categoryDentistService;

    @PostMapping("/dentist/address")
    public List<DentistDto> personalizedDenDis(
            @RequestBody PersonalizedDentLocDto personalizedDentLocDto
    ){
        return personalizedDentistService.personalizedDentistByDis(personalizedDentLocDto);
    }
    @PostMapping("/dentist/saved")
    public List<DentistDto> personalizedDenDisSaved(
            @RequestBody PersonalizedDentDto personalizedDentDto,
            @RequestHeader("Authorization") String token
    ){
        return personalizedDentistService.personalizedDentistByDisSaved(personalizedDentDto,token);
    }

    @PostMapping("/openDentist")
    public List<DentistDto> openDentist(
            @RequestBody LocationDto locationDto
    ){
        return openDentistService.openDentistNow(locationDto);
    }

    @PostMapping("/categoryDentist")
    public List<DentistDto> categoryDentist(
            @RequestBody CategoryDto categoryDto
    ){
        return categoryDentistService.categoryDentist(categoryDto);
    }
}
