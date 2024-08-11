package com.example.domain.dentist.controller;

import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.dentist.controller.model.LocationDto;
import com.example.domain.dentist.controller.model.PersonalizedDentistDTO;
import com.example.domain.dentist.service.OpenDentistService;
import com.example.domain.dentist.service.PersonalizedDentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/find")
public class PersonalizedDentistController {

    private final PersonalizedDentistService personalizedDentistService;
    private final OpenDentistService openDentistService;

    @PostMapping("/personalDentist")
    public List<DentistDto> personalizedDenDis(
            @RequestBody PersonalizedDentistDTO personalizedDentistDTO
    ){
        return personalizedDentistService.personalizedDentistByDis(personalizedDentistDTO);
    }

    @PostMapping("/openDentist")
    public List<DentistDto> openDentist(
            @RequestBody LocationDto locationDto
    ){
        return openDentistService.openDentistNow(locationDto);
    }
}
