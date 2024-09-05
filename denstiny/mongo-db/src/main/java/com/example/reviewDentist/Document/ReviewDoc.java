package com.example.reviewDentist.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

@Document(collection = "review")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewDoc {

    @Id
    private ObjectId id;

    @Field(name = "hospitalId")
    private String hospitalId;

    @Field(name = "date")
    private LocalDateTime date;

    @Field(name = "userId")
    private Long userId;

    @Field(name = "tag")
    private List<String> tag;

    @Field(name = "content")
    private String content;

    @Field(name = "depth")
    private int depth;

    @Field(name = "commentReplys")
    private List<ObjectId> commentReplys = new ArrayList<ObjectId>();

}
