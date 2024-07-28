package com.umc.naoman.domain.photo.dto;

import jakarta.validation.constraints.NotEmpty;
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

        @NotEmpty(message = "이미지의 이름은 하나 이상이어야 합니다.")
        private List<String> imageNameList;

    }

}
