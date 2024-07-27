package com.umc.naoman.domain.shareGroup.dto.validation.exception;

import com.umc.naoman.global.error.BusinessException;
import com.umc.naoman.global.error.code.ShareGroupErrorCode;

public class ShareGroupException extends BusinessException {

    public ShareGroupException(ShareGroupErrorCode errorCode) {
        super(errorCode);
    }
}
