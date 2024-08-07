package com.umc.naoman.global.result.code;

import com.umc.naoman.global.result.ResultCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteResultCode implements ResultCode {
    GENERATE_VOTE(200, "SV001", "특정 안건에 대하여 성공적으로 투표하였습니다."),
    GET_VOTE_LIST(200, "SV002", "특정 안건에 대한 투표 목록을 성공적으로 조회하였습니다."),
    DELETE_VOTE(200, "SV003", "특정 안건에 대한 투표를 성공적으로 삭제하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
