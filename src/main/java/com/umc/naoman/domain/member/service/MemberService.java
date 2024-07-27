package com.umc.naoman.domain.member.service;

import com.umc.naoman.domain.member.dto.MemberRequest.AndroidSignupRequest;
import com.umc.naoman.domain.member.dto.MemberRequest.WebSignupRequest;
import com.umc.naoman.domain.member.dto.MemberResponse.CheckMemberRegistration;
import com.umc.naoman.domain.member.dto.MemberResponse.LoginInfo;
import com.umc.naoman.domain.member.entity.Member;

public interface MemberService {
    Member findMember(Long memberId);
    Member findMember(String email);
    CheckMemberRegistration checkRegistration(String email);
    LoginInfo signup(AndroidSignupRequest request);
    LoginInfo signup(String tempMemberInfo, WebSignupRequest request);
    // MyPageInfo getMyPageInfo(Member member);
}
