package com.umc.naoman.domain.vote.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class VoteRequest {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateVoteListRequest {
        @NotEmpty(message = "최소 1개의 투표를 한 후 요청해야 합니다.")
        private List<GenerateVoteRequest> voteInfoList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenerateVoteRequest {
        @Size(max = 15, message = "투표 코멘트는 최대 15자까지 작성할 수 있습니다.")
        private String comment;
        @NotNull(message = "투표하려는 안건의 사진에 대한 agendaPhotoId 값을 필수로 입력해 주세요.")
        private Long agendaPhotoId;
    }
}
