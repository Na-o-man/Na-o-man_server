package com.umc.naoman.domain.agenda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class AgendaPhotoResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgendaPhotoInfo {
        private Long agendaPhotoId;
        private String url;
    }
}
