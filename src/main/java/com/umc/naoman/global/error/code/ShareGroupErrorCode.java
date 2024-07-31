package com.umc.naoman.global.error.code;

import com.umc.naoman.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShareGroupErrorCode implements ErrorCode {
    EMPTY_MEMBER_NAME_LIST(400, "EG001", "멤버 이름 리스트는 비어 있을 수 없습니다."),
    EMPTY_MEETING_TYPE_LIST(400, "EG002", "모임의 성격을 하나 이상 선택해야 합니다."),
    NULL_PLACE(400, "EG003", "모임 장소를 입력해야 합니다."),
    MEMBER_COUNT_MISMATCH(400, "EG004", "멤버 수와 멤버 이름 리스트의 크기가 일치하지 않습니다."),

    SHARE_GROUP_NOT_FOUND(404, "EG005", "공유 그룹을 찾을 수 없습니다."),

    PROFILE_NOT_FOUND(404, "EG006", "프로필을 찾을 수 없습니다."),
    INVALID_PROFILE_FOR_GROUP(400, "EG007", "해당 프로필은 이 공유 그룹에 속하지 않습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
