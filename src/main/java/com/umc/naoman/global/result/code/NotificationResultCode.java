package com.umc.naoman.global.result.code;

import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationResultCode implements ResultCode {
    //알림 관련 성공 코드
    GET_MY_NOTIFICATION(200,"SN001", "내 알림 목록을 성공적으로 조회하였습니다"),
    CHECK_MY_UNREAD_NOTIFICATION(200,"SN002", " 읽지 않은 알림 유무를 성공적으로 확인하였습니다"),
    READ_ALL_MY_NOTIFICATION(200,"SN003", "모든 알림을 성공적으로 읽었습니다"),
    DELETE_MY_NOTIFICATION(200,"SN003", "알람을 성공적으로 삭제했습니다"),
    REGISTER_FCM_TOKEN(200,"SN004", "FCM 토큰을 성공적으로 등록했습니다")
    ;


    private final int status;
    private final String code;
    private final String message;
}
