package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteErrorCode implements ErrorCode {
    VOTE_NOT_FOUND(404, "EV001", "해당 voteId를 가진 투표가 존재하지 않습니다."),
    DUPLICATE_VOTE(400, "EV002", "이미 해당 사진에 투표하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
