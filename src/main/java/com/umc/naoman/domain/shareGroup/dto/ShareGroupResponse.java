package com.umc.naoman.domain.shareGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

public abstract class ShareGroupResponse {

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupInfo {
        private Long shareGroupId;
        private String name; //공유그룹 이름 반환
        private String image; //공유그룹 대표 이미지 반환. 처음 공유 그룹 생성 시에는 null
        private int memberCount; //공유 그룹의 프로필 개수
        private String inviteUrl; //공유그룹 초대 url 반환
        private String inviteCode; //공유그룹 초대 코드 반환

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupDetailInfo extends ShareGroupInfo {
        private List<ProfileInfo> profileInfoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedShareGroupInfo {
        private List<ShareGroupInfo> shareGroupInfoList; //공유그룹 상세 정보 리스트
        private int page; // 페이지 번호
        private long totalElements; // 해당 조건에 부합하는 요소의 총 개수
        private boolean isFirst; // 첫 페이지 여부
        private boolean isLast; // 마지막 페이지 여부
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShareGroupNameInfo {
        private Long shareGroupId;
        private String name; //공유그룹 이름 반환
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedShareGroupNameInfo {
        private List<ShareGroupNameInfo> shareGroupNameInfoList; //공유그룹 상세 정보 리스트
        private int page; // 페이지 번호
        private long totalElements; // 해당 조건에 부합하는 요소의 총 개수
        private boolean isFirst; // 첫 페이지 여부
        private boolean isLast; // 마지막 페이지 여부
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

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InviteInfo {
        private Long shareGroupId;
        private String inviteCode;
        private String inviteUrl;
    }
}
