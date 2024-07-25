package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    FAILED_UPLOAD_S3(500, "ES3001", "S3에 업로드를 실패하였습니다.")

    ;

    private final int status;
    private final String code;
    private final String message;
}
