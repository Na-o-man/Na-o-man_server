package com.umc.naoman.domain.shareGroup.dto;

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
    public static class createShareGroupResponse extends ShareGroupResponse {
        Long shareGroupId;
        String name; //공유그룹 이름 반환
        String inviteCode; //공유그룹 초대 링크 반환
        LocalDateTime createdAt;
    }
}
