package com.umc.naoman.global.error;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ElasticsearchIOException extends RuntimeException {
    private final ErrorCode errorCode;

    public ElasticsearchIOException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public ElasticsearchIOException(Throwable cause, ErrorCode errorCode){
        super(cause);
        this.errorCode = errorCode;
    }

    public ElasticsearchIOException(String message, Throwable cause, ErrorCode errorCode){
        super(message, cause);
        this.errorCode = errorCode;
    }
}
