package com.umc.naoman.domain.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class PhotoResponse {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreSignedUrlListResponse {
        private List<PreSignedUrlResponse> preSignedUrlList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreSignedUrlResponse {
        private String preSignedUrl;
        private String imageUrl;
        private String imageName;
    }
}
