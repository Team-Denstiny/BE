package com.example.dentist.document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;



@Document(collection = "staticInfo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StaticInfoDoc {


//    @Field("_id") // JSON의 "_id"와 매핑
//    private String _id;
    @Id
    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("addr")
    private String addr;

    @Field("dong")
    private String dong;

    @Field("gu")
    private String gu;

    @Field("tele")
    private String tele;

    @Field("img")
    private String img;

    @GeoSpatialIndexed
    @Field("location") // JSON의 "location"과 매핑
    private Point location; // JSON의 location 필드는 GeoJSON 포맷의 Point 객체

    @Field("subway_info")
    private String subwayInfo;

    @Field("subway_name")
    private String subwayName;

    @Field("dist")
    private Integer dist;
}
