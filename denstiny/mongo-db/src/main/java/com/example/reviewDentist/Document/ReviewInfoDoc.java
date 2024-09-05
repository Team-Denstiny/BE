package com.example.reviewDentist.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "reviewInfo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewInfoDoc {

    @Id
    private String id;

    @Field("reviews")
    private List<ObjectId> reviews;
}
