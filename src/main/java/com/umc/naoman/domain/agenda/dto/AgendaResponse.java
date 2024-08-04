package com.umc.naoman.domain.agenda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public abstract class AgendaResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AgendaInfo {
        private Long agendaId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }
}
