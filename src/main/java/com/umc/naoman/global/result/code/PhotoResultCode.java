package com.umc.naoman.global.result.code;


import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PhotoResultCode implements ResultCode {
    CREATE_PRESIGNED_URL(200, "SP000", "성공적으로 Presigned URL을 요청하였습니다."),
    UPLOAD_PHOTO(200, "SP000", "성공적으로 이미지를 업로드하였습니다."),
    RETRIEVE_PHOTO(200, "SP000", "성공적으로 이미지를 조회하였습니다."),
    DELETE_PHOTO(200, "SP000", "성공적으로 이미지를 삭제하였습니다."),
    DOWNLOAD_PHOTO(200, "SP000", "성공적으로 이미지를 디운로드하였습니다.")

    ;
    private final int status;
    private final String code;
    private final String message;
}
