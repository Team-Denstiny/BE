package com.example.domain.dentist.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationGuDto {

    private Double latitude;
    private Double longitude;
    private String gu;
}
