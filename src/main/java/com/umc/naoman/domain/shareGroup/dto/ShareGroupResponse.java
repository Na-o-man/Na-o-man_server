package com.umc.naoman.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupDetailInfo {
        private Long shareGroupId;
        private String name; //공유그룹 이름 반환
        private String image; //공유그룹 대표 이미지 반환
        private int memberCount;
        private List<ProfileInfo> profileInfoList;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupId {
        private Long shareGroupId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileInfo {
        private Long profileId;
        private String name;
        private String image; //프로필 이미지 반환
        private Long memberId;
    }


}
