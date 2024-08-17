package com.example.dentist.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document(collection = "dynamicInfo")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DynamicInfoDoc {
    @Id
    private String _id;

    @Field("id")
    private String id;

    @Field("name")
    private String name;

    @Field("score")
    private Double score;

    @Field("review_cnt")
    private Integer reviewCnt;

    @Field("timeInfo")
    private Map<String, TimeData> timeDataMap;

    @Field("treat_cate")
    private List<String> category;


}
