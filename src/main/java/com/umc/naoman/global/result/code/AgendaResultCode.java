package com.umc.naoman.global.result.code;

import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgendaResultCode implements ResultCode {
    CREATE_AGENDA(200, "SA001", "새로운 안건을 성공적으로 생성하였습니다."),
    AGENDA_DETAIL(200, "SA002", "상세 안건을 성공적으로 조회했습니다.")
    ;
    private final int status;
    private final String code;
    private final String message;
}
