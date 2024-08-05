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
        @Size(min = 2, max = 6, message = "안건에 등록하는 사진은 최소 2개 최대 6개로 한정 시켜 주세요.")
        private List<Long> agendasPhotoList;
    }
}
