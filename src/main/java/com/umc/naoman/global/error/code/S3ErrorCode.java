package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3ErrorCode implements ErrorCode {
    FAILED_UPLOAD_S3(500, "ES3000", "S3에 업로드를 실패하였습니다."),
    PHOTO_NOT_FOUND_S3(404, "ES3000", "S3에서 파일을 찾을 수 없습니다."),
    UNAUTHORIZED_GET(403, "ES3000", "사진을 조회할 권한이 없습니다."),
    UNAUTHORIZED_DELETE(403, "ES3000", "사진을 삭제할 권한이 없습니다."),
    UNAUTHORIZED_UPLOAD(403, "ES3000", "사진을 업로드할 권한이 없습니다."),
    PHOTO_NOT_FOUND(404, "ES3000", "요청한 사진이 존재하지 않습니다."),
    FAILED_DOWNLOAD_PHOTO(500, "ES3000", "사진을 다운로드하는 도중 문제가 발생하였습니다.")

    ;

    private final int status;
    private final String code;
    private final String message;
}
