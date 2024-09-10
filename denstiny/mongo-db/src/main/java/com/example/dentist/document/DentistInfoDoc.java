package com.example.dentist.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.Map;

@Document(collection = "dentistInfo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DentistInfoDoc {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("addr")
    private String address;

    @Field("dong")
    private String dong;

    @Field("gu")
    private String gu;

    @Field("tele")
    private String telephone;

    @Field("img")
    private String imageUrl;
    @GeoSpatialIndexed
    @Field("location")
    private Point location; // 사용자가 지정한 좌표는 GeoJsonPoint로 매핑

    @Field("subway_info")
    private String subwayInfo;

    @Field("subway_name")
    private String subwayName;

    @Field("dist")
    private Integer distance;

    @Field("timeInfo")
    private Map<String, TimeData> timeInfo; // 요일별 시간 정보

    @Field("treat_cate")
    private List<String> treatmentCategories; // 치료 카테고리 목록


}

