package com.umc.naoman.domain.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public abstract class AgendaResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CreateAgenda {
        Long agendaId;
        LocalDateTime createdAt;
    }
}
