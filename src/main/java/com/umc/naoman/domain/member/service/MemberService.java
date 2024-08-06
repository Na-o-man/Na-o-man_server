package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.dto.MemberRequest.LoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.MarketingAgreedRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.SignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.dto.MemberResponse.MemberInfo;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;

public interface MemberService {
    Member findMember(Long memberId);
    Member findMember(String email);
    Member findMember(String authId, SocialType socialType);
    CheckMemberRegistration checkRegistration(LoginRequest request);
    LoginInfo signup(SignupRequest request);
    LoginInfo signup(String tempMemberInfo, MarketingAgreedRequest request);
    LoginInfo login(LoginRequest request);
    MemberInfo getMyInfo(Member member);
}
