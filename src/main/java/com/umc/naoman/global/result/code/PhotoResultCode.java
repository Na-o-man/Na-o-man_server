package com.umc.naoman.global.result.code;


import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoResultCode implements ResultCode {
    CREATE_SHARE_GROUP(200, "SP001", "성공적으로 Presigned URL을 요청하였습니다."),

    ;
    private final int status;
    private final String code;
    private final String message;
}
