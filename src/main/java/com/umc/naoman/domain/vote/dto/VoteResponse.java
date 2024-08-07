package com.umc.naoman.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class VoteResponse {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteId {
        private Long voteId;
    }
}
