package com.umc.naoman.global.result.code;

import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShareGroupResultCode implements ResultCode {
    CREATE_SHARE_GROUP(200, "SG001", "성공적으로 공유 그룹을 생성하였습니다."),
    SHARE_GROUP_INFO(200, "SG002", "공유 그룹을 성공적으로 조회했습니다."),
    JOIN_SHARE_GROUP(200, "SG003", "성공적으로 공유 그룹에 참여했습니다." )
    
    ;
    private final int status;
    private final String code;
    private final String message;
}
