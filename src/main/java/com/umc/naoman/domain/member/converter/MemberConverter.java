package com.umc.naoman.domain.member.converter;

import com.umc.naoman.domain.member.dto.MemberRequest.AndroidSignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberConverter {
    public Member toEntity(AndroidSignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .image(request.getImage())
                .socialType(request.getSocialType())
                .marketingAgreed(request.getMarketingAgreed())
                .build();
    }

    public LoginInfo toLoginInfo(Long memberId, String accessToken, String refreshToken) {
        return LoginInfo.builder()
                .memberId(memberId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
