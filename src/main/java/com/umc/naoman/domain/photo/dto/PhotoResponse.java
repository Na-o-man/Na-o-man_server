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
    public static class PreSignedUrlListInfo {
        private List<PreSignedUrlInfo> preSignedUrlInfoList;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PreSignedUrlInfo {
        private String preSignedUrl;
        private String photoUrl;
        private String photoName;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhotoUploadInfo {
        private Long shareGroupId;
        private int uploadCount;
    }
}
