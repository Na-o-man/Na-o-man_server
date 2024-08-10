package com.umc.naoman.domain.photo.elasticsearch.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
