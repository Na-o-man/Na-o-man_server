package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    MEMBER_NOT_FOUND_BY_MEMBER_ID(404, "EM001", "해당 memberId를 가진 회원이 존재하지 않습니다."),
    MEMBER_NOT_FOUND_BY_EMAIL(404, "EM002", "해당 이메일을 가진 회원이 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(404, "EM000", "해당 refresh token이 존재하지 않습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
