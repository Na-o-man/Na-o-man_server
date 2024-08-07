package com.umc.naoman.domain.vote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class VoteRequest {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateVoteRequest {
        // 코멘트 관련 길이 제한
        private String comment;
        @NotNull(message = "투표하려는 안건의 사진에 대한 agendaPhotoId 값을 필수로 입력해 주세요.")
        private Long agendaPhotoId;
    }
}
