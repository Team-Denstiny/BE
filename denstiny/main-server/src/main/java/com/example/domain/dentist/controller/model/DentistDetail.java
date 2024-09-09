package com.example.domain.dentist.controller.model;

import com.example.dentist.document.TimeData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DentistDetail {

    private String name;

    private String addr;

    private String gu;

    private String dong;

    private String tele;

    private String img;

    private String subwayInfo;

    private String subwayName;

    private Integer dist;

    private Map<String, TimeData> timeDataMap;

    private List<String> category;

}
