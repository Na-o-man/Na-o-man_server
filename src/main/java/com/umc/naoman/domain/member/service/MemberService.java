package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.dto.MemberRequest.AndroidLoginRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.AndroidSignupRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.WebSignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.entity.Member;
import com.umc.naoman.domain.member.entity.SocialType;

public interface MemberService {
    Member findMember(Long memberId);
    Member findMember(String email);
    Member findMember(Long authId, SocialType socialType);
    CheckMemberRegistration checkRegistration(String email);
    LoginInfo signup(AndroidSignupRequest request);
    LoginInfo signup(String tempMemberInfo, WebSignupRequest request);
    LoginInfo login(AndroidLoginRequest request);
    // MyPageInfo getMyPageInfo(Member member);
}
