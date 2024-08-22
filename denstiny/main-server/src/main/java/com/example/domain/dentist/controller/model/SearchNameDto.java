package com.example.domain.dentist.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchNameDto {

    private String name;
    private Double latitude;
    private Double longitude;

}
