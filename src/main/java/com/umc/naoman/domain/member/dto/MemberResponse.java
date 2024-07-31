package com.umc.naoman.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public abstract class MemberResponse {
    @Getter
    @AllArgsConstructor
    public static class CheckMemberRegistration {
        private Boolean isRegistered;
    }

    @Getter
    @AllArgsConstructor
    public static class MemberId {
        private Long memberId;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginInfo {
        private Long memberId;
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MemberInfo { //특정 회원 조회
        private String name;
        private String email;
        private String image;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MarketingAgreed {
        private Boolean marketingAgreed;
    }
}
