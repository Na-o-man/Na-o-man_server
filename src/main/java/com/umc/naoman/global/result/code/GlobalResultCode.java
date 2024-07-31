package com.umc.naoman.global.result.code;

import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalResultCode implements ResultCode {
    HEALTH_CHECK(200, "HEALTH_CHECK", "서버가 정상적으로 동작합니다.")

    ;

    private final int status;
    private final String code;
    private final String message;
}


