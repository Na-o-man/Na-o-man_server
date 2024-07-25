package com.umc.naoman.global.result.code;


import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberResultCode implements ResultCode {
    MYPAGE_INFO(200, "SM001", "내 정보를 성공적으로 조회하였습니다."),
    EDIT_MYPAGE_INFO(200, "SM002", "내 정보를 성공적으로 수정하였습니다."),
    CHARGE_MY_POINT(200, "SM003","포인트를 성공적으로 충전하였습니다."),
    GET_MY_POINT(200, "SM004", "내 포인트를 성공적으로 조회하였습니다."),
    LOGIN(200, "SM000", "성공적으로 로그인하였습니다."),

    ;
    private final int status;
    private final String code;
    private final String message;
}
