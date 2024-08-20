package com.example.reviewDentist.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "review")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDoc {

    @Id
    private ObjectId id;

    @Field(name = "hospital_id")
    private String hospitalId;

    @Field(name = "date")
    private LocalDateTime date;

    @Field(name = "user_id")
    private Long userId;

    @Field(name = "content")
    private String content;
}
