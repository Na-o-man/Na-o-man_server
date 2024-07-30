package com.umc.naoman.domain.photo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class PhotoRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreSignedUrlRequest {

        @NotEmpty(message = "사진의 이름은 하나 이상이어야 합니다.")
        private List<String> photoNameList;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoUploadRequest {

        @NotNull(message = "공유 그룹의 아이디 값을 입력해야 합니다.")
        private Long shareGroupId;
        @NotEmpty(message = "사진의 주소는 하나 이상이어야 합니다.")
        private List<String> photoUrlList;
    }

}
