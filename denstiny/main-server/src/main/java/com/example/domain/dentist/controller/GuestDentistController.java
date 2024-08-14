package com.example.domain.dentist.controller;

import com.example.domain.dentist.controller.model.CategoryLocDto;
import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.LocationDto;
import com.example.domain.dentist.controller.model.PersonalizedDentLocDto;
import com.example.domain.dentist.service.CategoryDentistService;
import com.example.domain.dentist.service.OpenDentistService;
import com.example.domain.dentist.service.PersonalizedDentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest/find")
public class GuestDentistController {

    private final PersonalizedDentistService personalizedDentistService;
    private final OpenDentistService openDentistService;
    private final CategoryDentistService categoryDentistService;

    @PostMapping("/dentist")
    public List<DentistDto> personalizedDenDis(
            @RequestBody PersonalizedDentLocDto personalizedDentLocDto
    ){
        return personalizedDentistService.personalizedDentistByDis(personalizedDentLocDto);
    }

    @PostMapping("/open-dentist")
    public List<DentistDto> openDentist(
            @RequestBody LocationDto locationDto
    ){
        return openDentistService.openDentistNow(locationDto);
    }

    @PostMapping("/cat-dentist")
    public List<DentistDto> categoryDentist(
            @RequestBody CategoryLocDto categoryLocDto
    ){
        return categoryDentistService.categoryDentist(categoryLocDto);
    }
}
