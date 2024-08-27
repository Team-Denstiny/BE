package com.example.domain.dentist.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

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

    private String subwayInfo;

    private String subwayName;

    private Integer dist;

    private List<String> todayWork;
}
