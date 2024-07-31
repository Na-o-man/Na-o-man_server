package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgendaErrorCode implements ErrorCode {
    AGENDA_NOT_FOUND_BY_AGENDA_ID(404, "EA001", "해당 agendaId를 가진 안건이 존재하지 않습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}