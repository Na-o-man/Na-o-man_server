package com.umc.naoman.domain.photo.elasticsearch.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "photos_es")
public class PhotoEs {
    @Id
    private String id;
    @Field(type = FieldType.Long)
    private Long shareGroupId;
    @Field(type = FieldType.Keyword)
    private String url;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Date)
    private String createdAt;
    @Field(type = FieldType.Long)
    private List<Long> faceTag;
    @Field(type = FieldType.Long)
    private List<Long> downloadTag;
}
