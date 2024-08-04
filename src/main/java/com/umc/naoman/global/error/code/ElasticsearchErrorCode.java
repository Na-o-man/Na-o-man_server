package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElasticsearchErrorCode implements ErrorCode {
    ELASTICSEARCH_IOEXCEPTION(500, "EE000", "Elasticsearch IOException 발생")
    ;
    private final int status;
    private final String code;
    private final String message;

}
