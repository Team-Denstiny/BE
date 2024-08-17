package com.example.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "staticInfo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaticInfoDoc {
    @Id
    private String _id;

    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("addr")
    private String addr;

    @Field("dong")
    private String dong;

    @Field("tele")
    private String tele;

    @Field("img")
    private String img;

    // 위도, 경도 double 타입으로 수정
    @Field("lon")
    private Double lon;

    @Field("lat")
    private Double lat;

    @Field("subway_info")
    private String subwayInfo;

    @Field("subway_name")
    private String subwayName;

    @Field("dist")
    private Integer dist;

}
