package com.umc.naoman.domain.photo.elasticsearch.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoEs {
    private Long rdsId;
    private Long shareGroupId;
    private String url;
    private String name;
    private String createdAt;
    private List<Long> faceTag;
    private List<Long> downloadTag;
}
