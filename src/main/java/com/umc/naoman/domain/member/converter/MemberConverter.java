package com.umc.naoman.domain.member.converter;

import com.umc.naoman.domain.member.dto.MemberRequest.SignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.HasSamplePhoto;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.dto.MemberResponse.MarketingAgreed;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberId;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberInfo;
import com.umc.naoman.domain.member.dto.MemberResponse;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
@Component
public class MemberConverter {
    public Member toEntity(SignupRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .image(request.getImage())
                .socialType(request.getSocialType())
                .authId(request.getAuthId())
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

    public SignupRequest toSignupRequest(Claims payload, boolean marketingAgreed) {
        return SignupRequest.builder()
                .email(payload.get("email", String.class))
                .name(payload.get("name", String.class))
                .image(payload.get("image", String.class))
                .socialType(SocialType.valueOf(payload.get("socialType", String.class)))
                .authId(payload.get("authId", String.class))
                .marketingAgreed(marketingAgreed)
                .build();
    }

    public MemberInfo toMemberInfo(Member member) {
        return MemberInfo.builder()
                .name(member.getName())
                .email(member.getEmail())
                .image(member.getImage())
                .build();
    }

    public MemberId toMemberId(Long memberId) {
        return new MemberId(memberId);
    }

    public CheckMemberRegistration toCheckMemberRegistration(boolean isRegistered) {
        return new CheckMemberRegistration(isRegistered);
    }

    public HasSamplePhoto toHasSamplePhoto(boolean hasSamplePhoto) {
        return new HasSamplePhoto(hasSamplePhoto);
    }

    public MarketingAgreed toMarketingAgreed(Member member) {
        return MarketingAgreed.builder()
                .marketingAgreed(member.getMarketingAgreed())
                .build();
    }

    public MemberResponse.DeleteMemberInfo toDeleteMemberInfo(Member member) {
        return MemberResponse.DeleteMemberInfo.builder()
                .memberId(member.getId())
                .deletedAt(member.getDeletedAt())
                .build();
    }
}
