package com.umc.naoman.domain.member.dto;

import com.umc.naoman.domain.member.entity.SocialType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public abstract class MemberRequest {
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebSignupRequest {
        // 별도의 데이터는 쿠키로 들어온다.
        @NotNull
        private Boolean marketingAgreed;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AndroidSignupRequest extends WebSignupRequest {
        @Email(message = "이메일의 경우, 올바른 이메일 형식으로 입력해야 합니다.")
        @NotBlank
        private String email;
        @NotBlank(message = "이름은 공백 또는 빈 문자열일 수 없습니다.")
        private String name;
        private String image;
        @NotNull(message = "socialType은 KAKAO, GOOGLE 중 하나를 입력해야 합니다.")
        private SocialType socialType;
        @NotNull(message = "authId는 소셜 플랫폼에서 제공한 회원 번호를 필수로 입력해야 합니다.")
        private Long authId;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AndroidLoginRequest {
        @NotNull(message = "로그인한 소셜 플랫폼의 회원 id를 입력해야 합니다.")
        private Long authId;
        @NotNull
        private SocialType socialType;

    }
}
