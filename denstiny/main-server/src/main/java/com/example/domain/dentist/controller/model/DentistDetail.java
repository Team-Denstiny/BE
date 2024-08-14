package com.example.domain.dentist.controller.model;

import com.example.document.TimeDataDoc;
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
    private String id;

    private String name;

    private String addr;

    private String dong;

    private String tele;

    private String img;

    private Double latitude;

    private Double longitude;

    private String subwayInfo;

    private String subwayName;

    private Integer dist;

    private Double score;

    private Integer reviewCnt;

    private Map<String, TimeDataDoc> timeDataMap;

    private List<String> category;
}
