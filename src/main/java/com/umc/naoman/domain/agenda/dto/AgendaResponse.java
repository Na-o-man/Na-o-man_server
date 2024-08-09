package com.umc.naoman.domain.agenda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.umc.naoman.domain.agenda.dto.AgendaPhotoResponse.AgendaPhotoInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public abstract class AgendaResponse {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class AgendaInfo {
        private Long agendaId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgendaDetailInfo {
        private Long agendaId;
        private String title;
        private List<AgendaPhotoInfo> agendaPhotoInfoList;
    }
}
