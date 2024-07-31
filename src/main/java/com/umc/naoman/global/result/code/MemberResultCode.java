package com.umc.naoman.global.result.code;

import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberResultCode implements ResultCode {
    SIGNUP(200, "SM000", "성공적으로 회원가입하였습니다."),
    LOGIN(200, "SM000", "성공적으로 로그인하였습니다."),
    MYPAGE_INFO(200, "SM001", "내 정보를 성공적으로 조회하였습니다."),
    EDIT_MYPAGE_INFO(200, "SM002", "내 정보를 성공적으로 수정하였습니다."),
    CHECK_MEMBER_REGISTRATION(200, "SM000", "해당 이메일을 가진 회원의 가입 여부를 성공적으로 조회하였습니다."),
    MEMBER_INFO (200,"SM005","회원 정보를 성공적으로 조회하였습니다."),
    CHECK_MARKETING_AGREED(200,"SM006","마케팅 동의 여부를 성공적으로 조회하였습니다."),
    ;
    private final int status;
    private final String code;
    private final String message;
}
