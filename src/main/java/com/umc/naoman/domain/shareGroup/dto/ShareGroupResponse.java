package com.umc.naoman.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public abstract class ShareGroupResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupInfo {
        private Long shareGroupId;
        private String name; //공유그룹 이름 반환
        private String inviteUrl; //공유그룹 초대 코드 반환
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }
}
