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
@Document(indexName = "sample_photo_vectors")
public class SamplePhotoVector {
    @Id
    private String id;
    @Field(type = FieldType.Long)
    private Long memberId;
    @Field(type = FieldType.Dense_Vector)
    private List<Float> faceVector;
}
