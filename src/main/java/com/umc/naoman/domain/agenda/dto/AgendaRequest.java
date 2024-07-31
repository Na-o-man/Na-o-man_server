package com.umc.naoman.domain.agenda.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public abstract class AgendaRequest {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAgendaRequest {
        @NotNull
        private Long shareGroupId;
        @NotNull
        private String title;
        @Size(min = 2, max = 6)
        private List<Long> agendasPhotoList;
    }
}
