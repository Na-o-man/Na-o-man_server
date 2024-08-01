package com.umc.naoman.domain.photo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PagedPhotoInfo {
        private List<PhotoInfo> photoInfoList;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhotoInfo {
        private String rawPhotoUrl;
        private String w200PhotoUrl;
        private String w400PhotoUrl;
        private LocalDateTime createdAt;
    }
}
