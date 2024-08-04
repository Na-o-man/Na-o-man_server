package com.umc.naoman.domain.photo.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "face_vectors")
public class FaceVector {
    @Id
    private String id;
    @Field(type = FieldType.Long)
    private String shareGroupId;
    @Field(type = FieldType.Keyword)
    private String keyValue;
    @Field(type = FieldType.Date)
    private String date;
    @Field(type = FieldType.Dense_Vector)
    private List<Float> faceVector;
}
