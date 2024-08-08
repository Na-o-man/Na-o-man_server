package com.umc.naoman.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse;
import com.umc.naoman.domain.shareGroup.dto.ShareGroupResponse.ProfileInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class VoteResponse {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteIdList {
        @Builder.Default
        private List<Long> voteIdList = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteId {
        private Long voteId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteInfo {
        private Long voteId;
        private String comment;
        private ProfileInfo profileInfo;
        private Long agendaPhotoId;
        private Boolean isMine;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime votedAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgendaPhotoVoteDetails {
        private Long agendaPhotoId;
        @Builder.Default
        private List<VoteInfo> voteInfoList = new ArrayList<>();
        private int voteCount;
    }
}
