package com.example.domain.dentist.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class DentistDto {

    private String id;

    private String name;

    private String addr;

    private String dong;

    private String tele;

    private String img;

    private Double latitude;

    private Double longitude;

    private Double score;

    private Integer reviewCnt;
}
