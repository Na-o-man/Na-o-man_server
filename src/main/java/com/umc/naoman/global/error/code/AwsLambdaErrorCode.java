package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AwsLambdaErrorCode implements ErrorCode {
    AWS_JsonProcessing_Exception(500, "EA000", "AWS Lambda JsonProcessingException 발생"),
    ;

    private final int status;
    private final String code;
    private final String message;
}
