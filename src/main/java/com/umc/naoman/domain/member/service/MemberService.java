package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.dto.MemberRequest.LoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.MarketingAgreedRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.SignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.HasSamplePhoto;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberId;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberInfo;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
    LoginInfo signup(String tempMemberInfo, MarketingAgreedRequest request, HttpServletResponse response);
    LoginInfo signup(SignupRequest request);
    LoginInfo login(LoginRequest request);
    CheckMemberRegistration checkRegistration(LoginRequest request);
    HasSamplePhoto hasSamplePhoto(Member member);
    MemberInfo getMyInfo(Member member);
    MemberId getMyMemberId(Member member);
    Member findMember(Long memberId);
    Member findMember(SocialType socialType, String authId);
    Member deleteMember(Member member);
}
