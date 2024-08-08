package com.umc.naoman.domain.vote.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
        private Long profileId;
        private Long agendaPhotoId;
        @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
        private LocalDateTime votedAt;
    }
}
