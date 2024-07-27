package com.umc.naoman.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.umc.naoman.global.error.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static com.umc.naoman.global.error.code.MemberErrorCode.INVALID_SOCIAL_TYPE;

@Getter
@RequiredArgsConstructor
public enum SocialType {
    KAKAO("id"),
    GOOGLE("sub");

    private final String usernameAttributeKey;

    @JsonCreator
    public static SocialType fromString(String value) {
        try {
            return SocialType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(INVALID_SOCIAL_TYPE);
        }
    }
}
