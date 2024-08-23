package com.example.domain.user_bookmarked.business;

import java.util.List;

import com.example.domain.dentist.controller.model.DentistDto;
import com.example.domain.user_bookmarked.converter.UserBookmarkedConverter;
import com.example.domain.user_bookmarked.service.UserBookmarkedService;

import annotation.Business;
import lombok.AllArgsConstructor;

@Business
@AllArgsConstructor
public class UserBookmarkedBusiness {

    private final UserBookmarkedService userBookmarkedService;
    private final UserBookmarkedConverter userBookmarkedConverter;


    public List<DentistDto> bookmarkedDentists(Long id){
        List<String> list = userBookmarkedService.readBookmarkedDentists(id);
        return userBookmarkedConverter.toDentistDtos(list);
    }
}
